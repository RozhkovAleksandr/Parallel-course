Этот проект представляет собой демонстрацию работы с инструментами Helgrind и ThreadSanitizer.

Оригинальный код взят из следующего репозитория:  [ParallelComputing](https://github.com/Nikitin-ve/ParallelComputing/tree/master/parallel_sorting/search)


# Helgrind


 - Запуск Helgrind

При обычном запуске ошибок гонок данных не обнаружено:

``` bash
==659== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 36 from 12)
```

 - Добавление гонки данных

После внесения изменений для искусственного создания гонок данных, Helgrind выводит следующие предупреждения:

Пример гонки данных:

```bash
==572== Possible data race during write of size 4 at 0x110014 by thread #4
==572== Locks held: none
==572==    at 0x10A698: search_function(void*) (в функции `search_function` в main2)
==572==    by 0x485396A: ??? (в `vgpreload_helgrind-amd64-linux.so`)
==572==    by 0x4B48AC2: start_thread (в `pthread_create.c`)
==572==    by 0x4BD9A03: clone (в `clone.S`)
==572==
==572== This conflicts with a previous write of size 4 by thread #3
==572== Locks held: none
==572==    at 0x10A698: search_function(void*) (в функции `search_function` в main2)
==572==    by 0x485396A: ??? (в `vgpreload_helgrind-amd64-linux.so`)
==572==    by 0x4B48AC2: start_thread (в `pthread_create.c`)
==572==    by 0x4BD9A03: clone (в `clone.S`)
==572==  Address 0x110014 is 0 bytes inside data symbol "thr"
```

# ThreadSanitizer

 - Запуск ThreadSanitizer

При обычном запуске ошибок гонок данных не обнаружено.

 - Добавление гонки данных

Внесем те же изменения, что и для Helgrind. ThreadSanitizer выводит следующие предупреждения:

Пример гонки данных:

```bash
WARNING: ThreadSanitizer: data race (pid=583)
  Write of size 4 at 0x561e5ed4e014 by thread T3:
    #0 search_function(void*) /mnt/c/Users/Conff/какие-то работы/ParallelComputing/parallel_sorting/search/main.cpp:27 (main3+0x27fb)

  Previous write of size 4 at 0x561e5ed4e014 by thread T1:
    #0 search_function(void*) /mnt/c/Users/Conff/какие-то работы/ParallelComputing/parallel_sorting/search/main.cpp:27 (main3+0x27fb)

  As if synchronized via sleep:
    #0 sleep ../../../../src/libsanitizer/tsan/tsan_interceptors_posix.cpp:352 (libtsan.so.0+0x66757)
    #1 search_function(void*) /mnt/c/Users/Conff/какие-то работы/ParallelComputing/parallel_sorting/search/main.cpp:26 (main3+0x27ec)

  Location is global 'thr' of size 4 at 0x561e5ed4e014 (main3+0x000000009014)
```

# Заключение
Thread Sanitizer и Helgrind взаимозаменяемые инструменты. Они оба нашли созданную гонку данных