1
```
(o1, o2) -> {
     int compareResult = o2.getValue().compareTo(o1.getValue());
     if (compareResult == 0) {
         return o1.getKey().compareTo(o2.getKey());
     } else {
         return compareResult;
     }
 }
```

```
Comparator.comparing(o -> o.getValue()).thenComparing(o -> o.getKey())
```


2
```
e -> e.getJobHistory()
               .stream()
                       .limit(1)
                       .collect(toList())
                       .get(0)
                       .getEmployer()
                       .equals("epam")
 ```