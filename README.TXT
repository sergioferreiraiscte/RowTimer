
BackEnd
=======

Dependencias:
  - Java :
    Versão : 1.8 da Oracle
  - Maven : 
    Em Linux ubuntu : apt-get install maven
  - ApacheTomcat
  - Eclipse
    Versão : Mars2
  - Git

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


