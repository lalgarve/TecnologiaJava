/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 *
 * @author leila
 * @param <T>
 */
public interface TabelaComDependentes<T extends ValorBD> extends TabelaBD<T> {
  Relacionamento1ParaN getRelacionamento1ParaN(String nome);
  Relacionamento1Para1 getRelacionamento1Para1(String nome);
}
