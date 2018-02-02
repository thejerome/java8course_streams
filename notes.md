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
 
 3 map: put or add if absent
```
if (m.containsKey(p.getKey())) {
    m.put(p.getKey(), m.get(p.getKey()) + duration);
} else {
    m.put(p.getKey(), duration);
}
 ```
 
```
map.merge(key, duration, Integer::sum);
```

4 combine two maps
```
(m1, m2) -> {
     final HashMap<String, Integer> map = new HashMap<>();
     map.putAll(m2);
     m1.forEach((key, value) -> map.merge(
             key,
             value,
             (v1, v2) -> v1 + v2
     ));
     return map;
 }
 ```
 
```
Stream.concat(m1.entrySet().stream(), m2.entrySet().stream())
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));
```
5 combine two maps wrong way
```
(map1, map2) -> {
   map1.putAll(map2);
   return map1;
}
 ```

6 optional
```
o -> {
     if (o.isPresent()) {
         return o.get().getPerson();
     }
     return new Person("emptyPerson",
             "emptyPerson", 0);
 }
 ```
 
```
o -> o.map(v -> v.getPerson()).orElse(new Person("emptyPerson", "emptyPerson", 0))
```

