package controller;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ThreadShowController extends Thread {
	
	private static int totalIngressos = 100;
	private Random tempoSistema;
	private int idUsuario;
	private Semaphore semaforo;
	
	public ThreadShowController(Random tempoSistema, int idUsuario, Semaphore semaforo) {
		this.tempoSistema = tempoSistema;
		this.idUsuario = idUsuario + 1;
		this.semaforo = semaforo;
	}
	
	@Override
	public void run() {
		login();
	}

	private void login() {
		int tempoLogin = tempoSistema.nextInt(2000) + 50;
		
		System.out.println("Usu�rio #" + idUsuario + " est� fazendo o login.");
		try {
			Thread.sleep(tempoLogin);
			if (tempoLogin > 1000)
				System.err.println("Login do usu�rio #" + idUsuario + " falhou!");
			else {
				System.out.println("Login do usu�rio #" + idUsuario + " realizado!");
				processoCompra();
			}							
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void processoCompra() {
		int tempoCompra = tempoSistema.nextInt(3000) + 1000;
		int qtdeIngressos = tempoSistema.nextInt(4) + 1;
		
		System.out.println("Processando a compra do usu�rio #"+ idUsuario);
		try {
			Thread.sleep(tempoCompra);
			if (tempoCompra > 2500)
				System.err.println("Tempo de sess�o expirou, compra do usu�rio #" + idUsuario + " falhou!");
			else {
				System.out.println("Aguardando valida��o da compra do usu�rio #" + idUsuario);
				validarCompra(qtdeIngressos);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void validarCompra(int qtdeIngressos) {
		try {
			semaforo.acquire();
			if (totalIngressos >= qtdeIngressos) {
				totalIngressos -= qtdeIngressos;
				System.out.println("Compra do usu�rio #" + idUsuario + " realizada com sucesso! \n"
						+ "Quantidade de ingressos comprados: " + qtdeIngressos + "\n"
								+ "Quantidade de ingressos dispon�veis: " + totalIngressos);
			} else {
				System.err.println("N�o foi poss�vel concluir a compra do usu�rio  " + idUsuario + "\n"
						+ "Motivo: Ingressos esgotados! At� a pr�xima.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaforo.release();
		}
	}
}
