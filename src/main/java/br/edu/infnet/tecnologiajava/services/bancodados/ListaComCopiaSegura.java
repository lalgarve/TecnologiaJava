package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

public class ListaComCopiaSegura <T extends Imutavel> implements List<T> {

    private List<T> lista;
    private boolean podeModificar;

    public ListaComCopiaSegura(){
        lista = new ArrayList<>();
        podeModificar = true;
    }

    /**
     * A lista interna é copiada automaticamente caso alguma alteração seja
     * executada.
     * 
     * @param listaASerCopiada
     */
    public ListaComCopiaSegura(ListaComCopiaSegura<T> listaASerCopiada){
        lista = listaASerCopiada.lista;
        podeModificar = false;
    }
    
    private void criaListaDelegadaSeNecessario(){
        if(!podeModificar){
            lista=new ArrayList<>(lista);
            podeModificar = true;
        }
    }

    public boolean add(T arg0) {
        criaListaDelegadaSeNecessario();
        return lista.add(arg0);
    }

    public void add(int arg0, T arg1) {
        criaListaDelegadaSeNecessario();
        lista.add(arg0, arg1);
    }

    public boolean addAll(Collection<? extends T> arg0) {
        criaListaDelegadaSeNecessario();
        return lista.addAll(arg0);
    }

    public boolean addAll(int arg0, Collection<? extends T> arg1) {
        criaListaDelegadaSeNecessario();
        return lista.addAll(arg0, arg1);
    }

    public void clear() {
        lista = new ArrayList<>();
        podeModificar = true;
    }

    public boolean contains(Object arg0) {
        return lista.contains(arg0);
    }

    public boolean containsAll(Collection<?> arg0) {
        return lista.containsAll(arg0);
    }

    public void forEach(Consumer<? super T> arg0) {
        lista.forEach(arg0);
    }

    public T get(int arg0) {
        return lista.get(arg0);
    }

    public int indexOf(Object arg0) {
        return lista.indexOf(arg0);
    }

    public boolean isEmpty() {
        return lista.isEmpty();
    }

    public Iterator<T> iterator() {
        criaListaDelegadaSeNecessario();
        return lista.iterator();
    }

    public int lastIndexOf(Object arg0) {
        return lista.lastIndexOf(arg0);
    }

    public ListIterator<T> listIterator() {
        criaListaDelegadaSeNecessario();
        return lista.listIterator();
    }

    public ListIterator<T> listIterator(int arg0) {
        criaListaDelegadaSeNecessario();
        return lista.listIterator(arg0);
    }

    public boolean remove(Object arg0) {
        criaListaDelegadaSeNecessario();
        return lista.remove(arg0);
    }

    public T remove(int arg0) {
        criaListaDelegadaSeNecessario();
        return lista.remove(arg0);
    }

    public boolean removeAll(Collection<?> arg0) {
        criaListaDelegadaSeNecessario();
        return lista.removeAll(arg0);
    }

    public void replaceAll(UnaryOperator<T> arg0) {
        criaListaDelegadaSeNecessario();
        lista.replaceAll(arg0);
    }

    public boolean retainAll(Collection<?> arg0) {
        criaListaDelegadaSeNecessario();
        return lista.retainAll(arg0);
    }

    public T set(int arg0, T arg1) {
        criaListaDelegadaSeNecessario();
        return lista.set(arg0, arg1);
    }

    public int size() {
        return lista.size();
    }

    public void sort(Comparator<? super T> arg0) {
        criaListaDelegadaSeNecessario();
        lista.sort(arg0);
    }

    public List<T> subList(int arg0, int arg1) {
        criaListaDelegadaSeNecessario();
        return lista.subList(arg0, arg1);
    }

    public Object[] toArray() {
        return lista.toArray();
    }

    public <C> C[] toArray(C[] arg0) {
        return lista.toArray(arg0);
    }

    public <C> C[] toArray(IntFunction<C[]> arg0) {
        return lista.toArray(arg0);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + lista.size();
        result = prime * result + lista.stream().limit(10).parallel().map((valor)->valor.hashCode()).reduce(0,(a,b)->a+b);
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
        ListaComCopiaSegura<?> other = (ListaComCopiaSegura<?>) obj;
        if (lista != other.lista){
            if(lista.size() != other.lista.size())
                return false;
            for(int i=0; i<lista.size(); i++){
                if(!lista.get(i).equals(other.lista.get(i)))
                    return false;
            }
        }
        return true;
    }  

    

    
}
