package com.quicktutorialz.nio.daos.v2;

import com.quicktutorialz.nio.entities.ToDo;
import com.quicktutorialz.nio.entities.ToDoDto;
import io.reactivex.Observable;

import java.util.*;

/**
 * @author alessandroargentieri
 *
 * Fake implementation of a non-blocking database
 */
public class ToDoDaoImpl implements ToDoDao{

    Map<String, ToDo> toDos;

    /* singleton implementation */
    private static ToDoDao instance;
    public static ToDoDao getInstance(){
        return (instance !=null) ? instance : new ToDoDaoImpl() ;
    }
    private ToDoDaoImpl(){
        initializeDB();
    }

    @Override
    public Observable<ToDo> create(ToDoDto dto) {
        ToDo todo = new ToDo(dto.getTitle(), dto.getDescription());
        toDos.put(todo.getId(), todo);
        return Observable.just(todo);
    }

    @Override
    public Observable<Optional<ToDo>> read(String id) {
        return Observable.just(Optional.ofNullable(toDos.get(id)));
    }

    @Override
    public Observable<List<ToDo>> readAll() {
        return Observable.fromCallable(() -> new ArrayList<>(toDos.values()));
        //return Observable.just(toDos.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList()));
    }

    @Override
    public Observable<Optional<ToDo>> update(ToDo toDo) {
        return toDos.get(toDo.getId())!=null ? Observable.just(Optional.of(toDos.replace(toDo.getId(), toDo))) :  Observable.just(Optional.empty()) ;
    }

    @Override
    public Observable<Boolean> delete(String id) {
        if(toDos.get(id)!=null) {
            toDos.remove(id);
            return Observable.just(true);
        }
        return Observable.just(false);
    }

    private void initializeDB(){
        toDos = new HashMap<>();
        ToDo todo1 = new ToDo("study reactive", "learn reactive programming");
        toDos.put(todo1.getId(), todo1);
        ToDo todo2 = new ToDo("learn ReactiveJ", "learn to use ReactiveJ library");
        toDos.put(todo2.getId(), todo2);
        ToDo todo3 = new ToDo("exercise", "do some exercises");
        toDos.put(todo3.getId(), todo3);
    }

    private ToDo updateToDo(ToDo todo, ToDoDto dto){
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        return todo;
    }
}