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
		
		System.out.println("Usuário #" + idUsuario + " está fazendo o login.");
		try {
			Thread.sleep(tempoLogin);
			if (tempoLogin > 1000)
				System.err.println("Login do usuário #" + idUsuario + " falhou!");
			else {
				System.out.println("Login do usuário #" + idUsuario + " realizado!");
				processoCompra();
			}							
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void processoCompra() {
		int tempoCompra = tempoSistema.nextInt(3000) + 1000;
		int qtdeIngressos = tempoSistema.nextInt(4) + 1;
		
		System.out.println("Processando a compra do usuário #"+ idUsuario);
		try {
			Thread.sleep(tempoCompra);
			if (tempoCompra > 2500)
				System.err.println("Tempo de sessão expirou, compra do usuário #" + idUsuario + " falhou!");
			else {
				System.out.println("Aguardando validação da compra do usuário #" + idUsuario);
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
				System.out.println("Compra do usuário #" + idUsuario + " realizada com sucesso! \n"
						+ "Quantidade de ingressos comprados: " + qtdeIngressos + "\n"
								+ "Quantidade de ingressos disponíveis: " + totalIngressos);
			} else {
				System.err.println("Não foi possível concluir a compra do usuário  " + idUsuario + "\n"
						+ "Motivo: Ingressos esgotados! Até a próxima.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaforo.release();
		}
	}
}
