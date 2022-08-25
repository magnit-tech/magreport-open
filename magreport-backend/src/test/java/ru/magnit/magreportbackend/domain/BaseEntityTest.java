package ru.magnit.magreportbackend.domain;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseEntityTest {

    @BeforeAll
    protected void initTests() throws Exception {
        checkCollectionsInitialized();
        checkLongConstructor();
        checkSetters();
    }

    /**
     * Проверка инициализации коллекций значением Collections.empty - List, Set, и т.д.
     *
     * @throws ClassNotFoundException - если тестируемый класс не найден
     */
    private void checkCollectionsInitialized() throws ClassNotFoundException {
        Class<?> clazz = Class.forName(this.getClass().getName().replace("Test", ""));
        Class<?> collectionType = java.util.Collection.class;

        Object objectUnderTest;
        try {
            objectUnderTest = clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            log.error("Error trying to create instance of " + clazz.getName(), ex);
            throw new RuntimeException("Error trying to create instance of " + clazz.getName(), ex);
        }

        Stream.of(clazz.getDeclaredFields())
                .filter(field -> collectionType.isAssignableFrom(field.getType()))
                .forEach(field -> {
                    String getterName = "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
                    try {
                        Method method = clazz.getDeclaredMethod(getterName);
                        assertNotNull(method.invoke(objectUnderTest), "Field " + clazz.getSimpleName() + "." + field.getName() + " must be initialized with Collections.empty");
                    } catch (Exception ex) {
                        log.error("Can't find or execute method: " + getterName, ex);
                        throw new RuntimeException("Can't find or execute method: " + getterName, ex);
                    }
                });
    }

    protected void checkLongConstructor() throws NoSuchMethodException, ClassNotFoundException {
        Class<?> clazz = Class.forName(this.getClass().getName().replace("Test", ""));

        var longConstructor = clazz.getDeclaredConstructor(Long.class);
        assertNotNull(longConstructor, "There is no constructor with Long parameter in class " + clazz.getSimpleName());
    }

    protected void checkNumberOfFields(int numFields) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(this.getClass().getName().replace("Test", ""));

        assertEquals(numFields, getAllClassFields(clazz).size(), "Number of fields in class " + clazz.getSimpleName() + " changed.");
    }

    /***
     * Сеттеры тестируемого класса проверяются на возвращение экземпляра тестируемого класса
     * для собственных и унаследованных полей
     * @throws ClassNotFoundException - тестируемый класс не найде
     */
    protected void checkSetters() throws ClassNotFoundException {
        var clazz = Class.forName(this.getClass().getName().replace("Test", ""));
        var fields = getAllClassFields(clazz);
        var setters = getAllSetters(clazz);

        var numWrongSetters = fields.stream()
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> !field.isAnnotationPresent(CreationTimestamp.class) && !field.isAnnotationPresent(UpdateTimestamp.class))
                .filter(field -> !setters.contains(getSetterName(field.getName())))
                .peek(field -> log.info("Field <" + field.getName() + "> has incorrect setter."))
                .count();

        if (numWrongSetters != 0 ) throw new RuntimeException("");
    }

    private Set<String> getAllSetters(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("set"))
                .filter(method -> clazz.isAssignableFrom(method.getReturnType()))
                .map(Method::getName)
                .collect(Collectors.toSet());
    }

    private List<Field> getAllClassFields(Class<?> clazz){
        var fields = new LinkedList<Field>();
        var isSuperClass = new AtomicBoolean(false);

        while (clazz.getSuperclass() != null) {
            fields.addAll(Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !isSuperClass.get() || (Modifier.isPublic(field.getModifiers()) || Modifier.isProtected(field.getModifiers())))
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toList())
            );
            clazz = clazz.getSuperclass();
            isSuperClass.set(true);
        }

        return fields;
    }

    private String capitalize(String string) {

        return string.substring(0,1).toUpperCase() + string.substring(1);
    }

    private String getSetterName(String fieldName) {

        return "set" + capitalize(fieldName);
    }}
