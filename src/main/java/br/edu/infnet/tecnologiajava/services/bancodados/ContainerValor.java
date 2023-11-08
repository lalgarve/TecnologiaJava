/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 *
 * @author leila
 */
class ContainerValor<T extends Cloneable> {
  private T valor;
  private int numeroReferencias;

  public T getValor() {
    return valor;
  }

  public void setValor(T valor) {
    this.valor = valor;
  }

  public int getNumeroReferencias() {
    return numeroReferencias;
  }

  void adicionaReferencia() {
    this.numeroReferencias++;
  }
  
  void removeReferencia() {
    this.numeroReferencias--;
  }  
}
