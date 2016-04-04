
BackEnd
=======

Dependencias:
  maven : 
    Em Linux ubuntu : apt-get install maven
  eclipse

Para importar para eclipse:
  File->Import->Maven->Existing Maven Project
  Next
  Root Directory->Browse
  Escolher "RowTimer/RowTimerBackOffice"
  OK Finsish 

Para ver se tudo está bem:
  No eclipse : Right Mouse Click->Maven->Update Project
  Na linha de comando: 
    $ cd RowTimer/RowTimerBackOffice
    $ mvn clean install


