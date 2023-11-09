/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 *
 * @author leila
 */
public interface ValorBD<C>{
  C getChave();
  ValorBD<C> getClone();
}
