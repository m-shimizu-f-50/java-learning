package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class TaskServiceTest {

  @Test
  /* getAll_returnsAllTasksFromRepository: mock(TaskRepository.class)で偽物を作り、when(fakeRepository.findAll(pageable)).thenReturn(...)で戻り値を設定。taskService.getAll(pageable)を呼び、返ってきたPageの中身が期待通りかassertEqualsで確認
  */
  void getAll_returnsAllTasksFromRepository() {
    // ①「本物のTaskRepositoryの代わりに使う、**偽物（モック）**を作ってください」という命令。fakeRepositoryはまだ何も設定されていない、空っぽの偽物。
    TaskRepository fakeRepository = mock(TaskRepository.class);
    // ②ページネーション対応後、findAll()はPageableを受け取りPage<Task>を返すようになったため、台本もそれに合わせる
    Pageable pageable = Pageable.unpaged();
    when(fakeRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(
        new Task("Task 1", false),
        new Task("Task 2", true))));

    // ③TaskServiceのコンストラクタにfakeRepositoryを渡す。これでTaskServiceは本物のDBにアクセスせず、fakeRepositoryを通してモックのデータを返すようになる。
    TaskService taskService = new TaskService(fakeRepository);
    // ④taskService.getAll(pageable)を呼ぶと、fakeRepository.findAll(pageable)が呼ばれ、②で設定したPageが返ってくる。
    Page<Task> result = taskService.getAll(pageable);
    List<Task> tasks = result.getContent();

    // ⑤返ってきたリストの中身をassertEqualsで確認。Task 1とTask 2が正しく返ってきているか、doneの状態も確認。
    assertEquals("Task 1", tasks.get(0).getTitle());
    assertFalse(tasks.get(0).isDone());
    assertEquals("Task 2", tasks.get(1).getTitle());
    assertTrue(tasks.get(1).isDone());
  }
  
  /*
  * create_savesNewTaskWithGivenTitle: fakeRepositoryをモックとして作り、taskService.create(title)を呼ぶ。verify(fakeRepository).save(createdTask)で、create()が呼ばれたときにfakeRepository.save()が呼ばれたか確認。さらに、作成されたタスクのタイトルとdoneの状態をassertEqualsとassertFalseで確認。
  */
  @Test
  void create_savesNewTaskWithGivenTitle() {
    TaskRepository fakeRepository = mock(TaskRepository.class);
    TaskService taskService = new TaskService(fakeRepository);

    String title = "New Task";
    Task createdTask = taskService.create(title);

    // verifyでfakeRepository.save()が呼ばれたか確認
    verify(fakeRepository).save(createdTask);

    // 作成されたタスクのタイトルとdoneの状態を確認
    assertEquals(title, createdTask.getTitle());
    assertFalse(createdTask.isDone());
  }

  /*
  * delete_callsRepositoryDeleteById: taskService.delete(1)を呼び、verify(fakeRepository).deleteById(1)で正しく呼ばれたか確認
   */
  @Test
  void delete_callsRepositoryDeleteById() {
     // ① 偽物のRepositoryを用意する（台本の設定は不要）
     TaskRepository fakeRepository = mock(TaskRepository.class);

     // ② その偽物をTaskServiceに注入する
     TaskService taskService = new TaskService(fakeRepository);
 
     // ③ 実際にdeleteを呼ぶ
     taskService.delete(1);
 
     // ④ 「fakeRepositoryのdeleteById(1)が、実際に呼ばれたか」を検証する
     verify(fakeRepository).deleteById(1);
  }
}