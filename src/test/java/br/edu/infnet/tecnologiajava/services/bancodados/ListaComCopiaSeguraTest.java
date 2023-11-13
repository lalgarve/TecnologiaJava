package br.edu.infnet.tecnologiajava.services.bancodados;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ListaComCopiaSeguraTest {

    private static List<Dados> listaDados1;
    private static List<Dados> listaDados2;

     private static class Dados implements Imutavel, Comparable<Dados>{
        private final int id;
        private final  String descricao;
        Dados(int id, String descricao) {
            this.id = id;
            this.descricao = descricao;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Dados other = (Dados) obj;
            if (id != other.id)
                return false;
            if (descricao == null) {
                if (other.descricao != null)
                    return false;
            } else if (!descricao.equals(other.descricao))
                return false;
            return true;
        }
        @Override
        public int compareTo(Dados arg0) {
            if(id!=arg0.id){
                return id-arg0.id;
            }else{
                return descricao.compareTo(arg0.descricao);
            }
        }
        
    }   

    @BeforeAll
    public static void inicializaListaValores(){
        listaDados1 = new ArrayList<>();
        listaDados2 = new ArrayList<>();

        for(int i=0; i<5; i++){
            Dados dados;
            dados = new Dados(i, "Dados Lista 1 - i="+i);
            listaDados1.add(dados);
            dados = new Dados(i, "Dados Lista 2 - i="+i);
            listaDados2.add(dados);         
        }
    }



    @Test
    public void testAdd() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.add(listaDados2.get(0));
        assertFalse(listaOri.contains(listaDados2.get(0)), "Lista Original não poderia ter sido modificada.");
        assertTrue(listaCopia.contains(listaDados2.get(0)), "Elemento não foi adicionado corretamente.");
        assertTrue(listaCopia.containsAll(listaOri));
    }

    @Test
    public void testAdd2() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.add(2,listaDados2.get(0));
        assertEquals(listaDados2.get(0), listaCopia.get(2));
        assertEquals(listaDados1.get(2), listaOri.get(2));
    }

    @Test
    public void testAddAll() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.addAll(listaDados2);
        assertTrue(listaCopia.containsAll(listaDados2));
        assertEquals(listaDados1.size(), listaOri.size());
    }

    @Test
    public void testAddAll2() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.addAll(3, listaDados2);
        assertTrue(listaCopia.containsAll(listaDados2));
        assertEquals(listaDados1.size(), listaOri.size());
        assertEquals(listaDados2.get(0), listaCopia.get(3));
    }

    @Test
    public void testClear() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.clear();
        assertTrue(listaCopia.isEmpty());
        assertFalse(listaOri.isEmpty());
    }



    @Test
    public void testIterator() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        Iterator<Dados> iterator = listaCopia.iterator();
        while(iterator.hasNext()){
            Dados dado = iterator.next();
            if(dado.id==1 || dado.id==4){
                iterator.remove();
            }
        }
        assertEquals(3, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    public void testLastIndexOf() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        listaOri.addAll(listaDados1);
        int lastIndexOf = listaOri.lastIndexOf(listaDados1.get(4));
        assertEquals(9, lastIndexOf);
    }

    @Test
    public void testListIterator() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        ListIterator<Dados> iterator = listaCopia.listIterator();
        while(iterator.hasNext()){
            Dados dado = iterator.next();
            if(dado.id==1 || dado.id==4){
                iterator.remove();
            }
        }
        assertEquals(3, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    public void testListIterator2() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        ListIterator<Dados> iterator = listaCopia.listIterator(2);
        while(iterator.hasNext()){
            Dados dado = iterator.next();
            if(dado.id==1 || dado.id==4){
                iterator.remove();
            }
        }
        assertEquals(4, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    public void testRemove() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        Dados dados = new Dados(1,"Dados Lista 1 - i=1");
        listaCopia.remove(dados);
        assertFalse(listaCopia.contains(listaDados1.get(1)));
        assertEquals(4, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    public void testRemove2() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.remove(1);
        assertFalse(listaCopia.contains(listaDados1.get(1)));
        assertEquals(4, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    public void testRemoveAll() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        List<Dados> listaRemove = listaDados1.subList(1, 3);
        listaCopia.removeAll(listaRemove);
        assertFalse(listaCopia.containsAll(listaRemove));
        assertTrue(listaOri.containsAll(listaRemove));
        assertEquals(3, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    public void testReplaceAll() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.replaceAll((dados) -> listaDados2.get(dados.id));
        assertArrayEquals(listaDados2.toArray(), listaCopia.toArray());
        assertTrue(listaOri.containsAll(listaDados1));
    }

    @Test
    void testRetainAll() {

    }

    @Test
    void testSet() {

    }

    @Test
    void testSize() {

    }

    @Test
    void testSort() {

    }

    @Test
    void testSubList() {

    }

    @Test
    void testToArray() {

    }

    @Test
    void testToArray2() {

    }

    @Test
    void testToArray3() {

    }
}
