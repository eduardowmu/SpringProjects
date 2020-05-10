package com.edu.demoajax.web.dwr;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.edu.demoajax.web.repository.PromocaoRepository;
//sta anotação transforma a classe em um Bean gerenciado pelo spring
@Component
@RemoteProxy//configuração de proxy remoto que será usado para comunicação entre servidor e cliente via DWR 
public class DWRAlertaPromocoes//para uso das consultas criadas anteriormente
{	@Autowired private PromocaoRepository repository;
	
	//este atributo será usado para agendamento de tarefas
	private Timer timer;
	
	private LocalDateTime getDtCadastroByUltimaPromocao()//desde a primeira página
	{	PageRequest pageRequest = PageRequest.of(0, 1, Direction.DESC, "cadastro");
		return this.repository.findUltimaDataDePromocao(pageRequest).getContent().get(0);
	}
	
	//essa notação tem como responsabilidade fazer a configuração de relação entre o método init
	//que temos no servidor e aquele método que declaramos no cliente depois do nome da classe.
	@RemoteMethod
	public synchronized void init()
	{	System.out.println("DWR ACTIVATED");//apenas para termos certeza que o método está sendo executado
		
		LocalDateTime lasDate = this.getDtCadastroByUltimaPromocao();
		
		WebContext context = WebContextFactory.get();
		
		timer = new Timer();
		
		//método de agendamento de tarefas. O primeiro tempo é execução de cada tarefa pela 1ª vez
		//o Segundo é de cada minuto será executado a tarefa
		timer.schedule(new AlertTask(context, lasDate), 10000, 60000);
	}
	
	//classe interna, que será a classe que trabalha junto com a Classe Timer para a realização
	//da tarefa 
	class AlertTask extends TimerTask
	{	private LocalDateTime lastDate;
	
		private WebContext context;
		
		private long count;
		
		public AlertTask(WebContext context, LocalDateTime lastDate) 
		{	this.lastDate = lastDate;
			this.context = context;
		}
		
		//método de agendamento de tarefa
		@Override public void run()
		{	String session = context.getScriptSession().getId();
			
			//é por este id de sessão que a DWR vai conseguir saber para onde precisa enviar as 
			//informações que o servidor quer atualizar o cliente
			Browser.withSession(context, session, new Runnable()
			{	//thread da própria DWR para que ela possa trabalhar com o recurso do AJAX Reverso.
				@Override public void run()
				{	Map<String, Object> map = repository.totalAndUltimaPromocaoByDataCadastro(lastDate);
				
					count = (Long)map.get("count");
					
					lastDate = map.get("lastDate") == null ? lastDate : (LocalDateTime)map.get("lastDate"); 
					
					Calendar time = Calendar.getInstance();
					
					time.setTimeInMillis(context.getScriptSession().getLastAccessedTime());
					System.out.println("count: " + count + ", lastDate: " + lastDate + "<" + session + ">" +
							"<" + time.getTime() + "<");
					
					if(count > 0)//classe da DWR, que implementa o métod abaixo, que permite receber nomes de método JS
					{ScriptSessions.addFunctionCall("showButton", count);}
				}
			});
		}
	}
}