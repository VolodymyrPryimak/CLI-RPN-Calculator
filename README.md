# CLI-RPN-Calculator
Command-line reverse polish notation (RPN) calculator

Running an App with Maven or an Executable Jar
-----------------
Prerequisites:
- Use maven for building a jar.<br>
- It requires JAVA 8 for running.
- Edit default properties for your needs:
```yaml
rpn:
  defaultStackSize: 10
     shell:
       decimalFormat: "#0.00"
```

There are two approaches for running it:
1. Use `mvn spring-boot:run` command in root directory.
2. Use `java -jar` command after project was built from `/target` directory.

Example:
```$xslt
$ mvn clean package spring-boot:repackage
$ java -jar target/${JarFileName}.jar
``` 

## Shell API:

| Function  | Parameter  | Type of parameter | Description |
|---|---|--------|-----|
| **calc**  | --in | String like "3 5 -". Supported operations: '+/*-' | RPN calculation. |
| **reset** | -/- | -/- | Empty stack. |
| **setLimitOfStack** | --limit  | Number | Set new size of stack. |
