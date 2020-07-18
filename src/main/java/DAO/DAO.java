package DAO;

import java.util.List;
import java.util.Set;

public interface DAO<T> {
    List<T> GetAll();

    T GetById(int id);

    void Save(T t);

    void Update(T t);

    void Delete(T t);
}
