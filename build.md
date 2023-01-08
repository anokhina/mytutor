## Project build

Build for production

```
mvn clean install -e -DskipTests=true -Pproduction > ../err.txt 2>&1
```

## Upload sources to .m2


```
 mvn dependency:sources
```
