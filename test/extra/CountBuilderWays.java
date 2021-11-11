package extra;

import es.utils.mapper.Mapper;
import es.utils.mapper.factory.builder.*;
import es.utils.mapper.impl.object.ClassMapper;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CountBuilderWays {

    private final static Class<?>[] STEPS = new Class<?>[]{
            Name.class,
            From.class,
            DefaultInput.class,
            Transformer.class,
            DefaultOutput.class,
            To.class,
            Consume.class,
            Builder.class
    };

    private static int[][] weights;
    private static Map<Class<?>,List<Method>> usedMethods;
    private static int[] ways;

    public static void main(String[] args) {
        countWays();
        printMatrix();
        printUsedMethod();
        printWays();
    }

    private static void countWays() {
        Map<Class<?>,Collection<String>> ignoreMethodsForStep = new HashMap<>();
        usedMethods = new LinkedHashMap<>();
        countWays(ignoreMethodsForStep);
    }
    private static void countWays(Map<Class<?>,Collection<String>> ignoreMethodsForStep) {
        int n = STEPS.length;
        initWays();
        weights = new int[n][n];
        for(int i=0; i<n-1; i++) {  // ciclo DA (step di provvenienza)
            Method[] methods = STEPS[i].getDeclaredMethods();
            for(Method method : methods) {
                if(!ignoreMethodsForStep.getOrDefault(STEPS[i],new HashSet<>()).contains(method.getName())) {
                    Class<?> destination = method.getReturnType();
                    for(int j=i+1; j<n; j++) { // ciclo A (step di destinazione)
                        if(STEPS[j].isAssignableFrom(destination)) {
                            usedMethods.merge(STEPS[i],new ArrayList<>(Arrays.asList(method)),(a, b)->{a.addAll(b); return a;});
                            weights[i][j]++;
                        }
                    }
                }
            }
            for(int j=i; j<n; j++) { // ciclo A
                ways[j] += ways[i]*weights[i][j];
            }
        }
    }

    private static void initWays() {
        int n = STEPS.length;
        ways = new int[n];
        try {
            Method startingMethod = EMBuilder.class.getDeclaredMethod("using", Mapper.class, ClassMapper.class);
            Class<?> destination = startingMethod.getReturnType();
            for (int i = 0; i < n; i++) {
                if (STEPS[i].isAssignableFrom(destination)) {
                    ways[i]++;
                }
            }
        } catch (NoSuchMethodException e) {
            ways[0] = 1; // di base solo il primo step è l'ingresso
        }
    }

    private static void printWays() {
        String[] names = Arrays.stream(STEPS).map(Class::getSimpleName).toArray(String[]::new);
        for(int step=1; step<STEPS.length; step++) {
            System.out.println(MessageFormat.format("Modi diversi possibili da {0} a {1}: {2}",
                    names[0],
                    names[step],
                    ways[step]));
        }
    }

    private static void printMatrix() {
        String[] headers = Arrays.stream(STEPS).map(Class::getSimpleName).toArray(String[]::new);
        int[] widths = new int[headers.length];
        widths[0] = (headers[0].length()+1)/2;
        int maxWidth = Arrays.stream(headers).mapToInt(String::length).max().orElse(0);
        for(int i=1; i<headers.length; i++) {
            widths[i] = (headers[i-1].length()+headers[i].length()+1)/2;
        }
        String splitter = "DA \\ A";
        System.out.printf("%"+maxWidth+"s ",splitter);
        Arrays.stream(headers).forEach(h->System.out.print(" "+h));
        System.out.println();
        for(int i=0; i<weights.length; i++) {
            System.out.printf("%"+maxWidth+"s ",headers[i]);
            for(int j=0; j<weights[i].length; j++) {
                System.out.printf("%"+(widths[j]+1)+"d", weights[i][j]);
            }
            System.out.println();
        }
    }

    private static void printUsedMethod() {
        try {
            usedMethods.put(STEPS[STEPS.length-1],Arrays.asList(STEPS[STEPS.length-1].getMethod("create")));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        for(Map.Entry<Class<?>,List<Method>> e : usedMethods.entrySet()) {
            StringBuilder sb = new StringBuilder(e.getKey().getSimpleName());
            sb.append(": ");
            int prefixLength = sb.length();
            String prefix = "\n";
            for(int i=0; i<prefixLength; i++) {
                prefix += " ";
            }
            StringJoiner sj = new StringJoiner(prefix);
            for(Method method : e.getValue()) {
                sj.add(Arrays.stream(method.getParameterTypes())
                             .map(Class::getSimpleName)
                             .collect(Collectors.joining(", ", method.getName()+"(",")"))
                      );
            }
            sb.append(sj);
            System.out.println(sb.toString());
        }
    }

}
