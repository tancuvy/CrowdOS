package cn.crowdos.kernel.constraint;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public interface Decomposer<T> {

    default List<T> decompose(){
        return trivialDecompose();
    }

    default Iterator<T> decomposerIterator(){
        return trivialDecomposerIterator();
    }

    default List<T> decompose(int scale) throws DecomposeException {
        return scaleDecompose(scale);
    }

    default Iterator<T> decomposerIterator(int scale) throws DecomposeException {
        return scaleDecomposerIterator(scale);
    }

    List<T> decompose(Function<T, List<T>> decomposeAction) throws DecomposeException;

    Iterator<T> decomposerIterator(Function<T, Iterator<T>> decomposerIteratorAction) throws DecomposeException;

    List<T> trivialDecompose();

    Iterator<T> trivialDecomposerIterator();

    List<T> scaleDecompose(int scale) throws DecomposeException;

    Iterator<T> scaleDecomposerIterator(int scale) throws DecomposeException;


}
