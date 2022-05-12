package cn.crowdos.kernel.constraint;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;


/**
 * <p>A decomposer and decompose a object that implement {@link Decomposable} into
 * multiple fine-grained parts.</p>
 *
 * <p>This interface provides two styles of access to decompositions.</p>
 * <ul>
 *     <li>Collection style that return a {@link List}.</li>
 *     <li>Iterator style that return a {@link Iterator}.</li>
 * </ul>
 *
 * @param <T> the type of decompositions returned by this decomposer.
 * @author loyx
 * @since 1.0.0
 * @see Decomposer
 */
public interface Decomposer<T> {

    /**
     * <p>Decompose a decomposable. {@link Decomposer#trivialDecompose()} by default.
     * @return List style decompositions.
     */
    default List<T> decompose(){
        return trivialDecompose();
    }

    /**
     * <p>Decompose a decomposable and return an iterator that can access compositions.
     * {@link Decomposer#trivialDecomposerIterator()} by default.
     * @return Iterator style decompositions.
     */
    default Iterator<T> decomposerIterator(){
        return trivialDecomposerIterator();
    }

    /**
     * <p>Decompose a decomposable by specified scale. {@link Decomposer#scaleDecompose(int)} by default.
     * @param scale specified decomposition scale.
     * @return List style decompositions.
     * @throws DecomposeException if any exceptions occur during decompose.
     */
    default List<T> decompose(int scale) throws DecomposeException {
        return scaleDecompose(scale);
    }

    /**
     * <p>Decompose a decomposable by specified scale an iterator that can access compositions.
     * {@link Decomposer#scaleDecomposerIterator(int)} by default.
     * @param scale specified decomposition scale.
     * @return Iterator style decompositions.
     * @throws DecomposeException if any exceptions occur during decompose.
     */
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
