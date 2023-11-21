package br.edu.infnet.tecnologiajava.services.bancodados;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;

public class ListaComCopiaSeguraTest {

    private static List<Dados> listaDados1;
    private static List<Dados> listaDados2;

    private static class Dados implements Imutavel {
        private final int id;
        private final String descricao;

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
                return other.descricao == null;
            } else return descricao.equals(other.descricao);
        }

    }

    @BeforeAll
    public static void inicializaListaValores() {
        listaDados1 = new ArrayList<>();
        listaDados2 = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Dados dados;
            dados = new Dados(i, "Dados Lista 1 - i=" + i);
            listaDados1.add(dados);
            dados = new Dados(i, "Dados Lista 2 - i=" + i);
            listaDados2.add(dados);
        }
    }


    @Test
    void testAdd() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.add(listaDados2.get(0));
        assertFalse(listaOri.contains(listaDados2.get(0)), "Lista Original não poderia ter sido modificada.");
        assertTrue(listaCopia.contains(listaDados2.get(0)), "Elemento não foi adicionado corretamente.");
        assertTrue(listaCopia.containsAll(listaOri));
    }

    @Test
    void testAdd2() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.add(2, listaDados2.get(0));
        assertEquals(listaDados2.get(0), listaCopia.get(2));
        assertEquals(listaDados1.get(2), listaOri.get(2));
    }

    @Test
    void testAddAll() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.addAll(listaDados2);
        assertTrue(listaCopia.containsAll(listaDados2));
        assertEquals(listaDados1.size(), listaOri.size());
    }

    @Test
    void testAddAll2() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.addAll(3, listaDados2);
        assertTrue(listaCopia.containsAll(listaDados2));
        assertEquals(listaDados1.size(), listaOri.size());
        assertEquals(listaDados2.get(0), listaCopia.get(3));
    }

    @Test
    void testClear() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.clear();
        assertTrue(listaCopia.isEmpty());
        assertFalse(listaOri.isEmpty());
    }


    @Test
    void testIterator() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        Iterator<Dados> iterator = listaCopia.iterator();
        while (iterator.hasNext()) {
            Dados dado = iterator.next();
            if (dado.id == 1 || dado.id == 4) {
                iterator.remove();
            }
        }
        assertEquals(3, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    void testLastIndexOf() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        listaOri.addAll(listaDados1);
        int lastIndexOf = listaOri.lastIndexOf(listaDados1.get(4));
        assertEquals(9, lastIndexOf);
    }

    @Test
    void testListIterator() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        ListIterator<Dados> iterator = listaCopia.listIterator();
        while (iterator.hasNext()) {
            Dados dado = iterator.next();
            if (dado.id == 1 || dado.id == 4) {
                iterator.remove();
            }
        }
        assertEquals(3, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    void testListIterator2() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        ListIterator<Dados> iterator = listaCopia.listIterator(2);
        while (iterator.hasNext()) {
            Dados dado = iterator.next();
            if (dado.id == 1 || dado.id == 4) {
                iterator.remove();
            }
        }
        assertEquals(4, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    void testRemove() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        Dados dados = new Dados(1, "Dados Lista 1 - i=1");
        listaCopia.remove(dados);
        assertFalse(listaCopia.contains(listaDados1.get(1)));
        assertEquals(4, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    void testRemove2() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.remove(1);
        assertFalse(listaCopia.contains(listaDados1.get(1)));
        assertEquals(4, listaCopia.size());
        assertEquals(5, listaOri.size());
    }

    @Test
    void testRemoveAll() {
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
    void testReplaceAll() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.replaceAll((dados) -> listaDados2.get(dados.id));
        assertArrayEquals(listaDados2.toArray(), listaCopia.toArray());
        assertTrue(listaOri.containsAll(listaDados1));
    }

    @Test
    void testRetainAll() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        List<Dados> listaRetain = new ArrayList<>();
        listaRetain.add(listaDados1.get(1));
        listaRetain.add(listaDados1.get(2));
        listaCopia.retainAll(listaRetain);
        Object[] esperado = listaRetain.toArray();
        Object[] resultado = listaCopia.toArray();
        assertArrayEquals(esperado, resultado);
        assertEquals(5, listaOri.size());
    }

    @Test
    void testSet() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.set(2, listaDados2.get(2));
        assertEquals(listaDados2.get(2), listaCopia.get(2));
        assertEquals(listaDados1.get(2), listaOri.get(2));
    }

    @Test
    void testSort() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        for (int i = listaDados1.size() - 1; i >= 0; i--) {
            listaOri.add(listaDados1.get(i));
        }
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        listaCopia.sort((dados1, dados2) -> dados1.id - dados2.id);
        assertArrayEquals(listaDados1.toArray(Dados[]::new), listaCopia.toArray(Dados[]::new));
        assertEquals(listaDados1.get(4), listaOri.get(0));
    }

    @Test
    void testSubList() {
        ListaComCopiaSegura<Dados> listaOri = new ListaComCopiaSegura<>();
        listaOri.addAll(listaDados1);
        ListaComCopiaSegura<Dados> listaCopia = new ListaComCopiaSegura<>(listaOri);
        List<Dados> sublista = listaCopia.subList(1, 3);
        sublista.set(0, listaDados2.get(0));
        assertArrayEquals(listaDados1.toArray(Dados[]::new), listaOri.toArray(Dados[]::new));
    }

}
