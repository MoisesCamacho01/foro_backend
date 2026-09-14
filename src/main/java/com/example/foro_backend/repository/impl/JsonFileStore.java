package com.example.foro_backend.repository.impl;

import com.example.foro_backend.exception.PersistenceException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

@Component
public class JsonFileStore {

    private final ObjectMapper objectMapper;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonFileStore(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void ensureFileExists(Path path) {
        lock.writeLock().lock();
        try {
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            if (Files.notExists(path)) {
                Files.writeString(path, "[]");
            }
        } catch (IOException ex) {
            throw new PersistenceException("No se pudo inicializar el archivo: " + path, ex);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public <T> List<T> readList(Path path, TypeReference<List<T>> typeReference) {
        ensureFileExists(path);
        lock.readLock().lock();
        try {
            byte[] bytes = Files.readAllBytes(path);
            if (bytes.length == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(bytes, typeReference);
        } catch (Exception ex) {
            throw new PersistenceException("No se pudo leer el archivo: " + path, ex);
        } finally {
            lock.readLock().unlock();
        }
    }

    public <T> void writeList(Path path, List<T> data) {
        ensureFileExists(path);
        lock.writeLock().lock();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), data);
        } catch (Exception ex) {
            throw new PersistenceException("No se pudo escribir el archivo: " + path, ex);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public <T> T readModifyWrite(Path path, TypeReference<List<T>> typeReference, Function<List<T>, T> modifier) {
        ensureFileExists(path);
        lock.writeLock().lock();
        try {
            List<T> items;
            byte[] bytes = Files.readAllBytes(path);
            if (bytes.length == 0) {
                items = new ArrayList<>();
            } else {
                items = new ArrayList<>(objectMapper.readValue(bytes, typeReference));
            }
            T result = modifier.apply(items);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), items);
            return result;
        } catch (Exception ex) {
            throw new PersistenceException("No se pudo actualizar el archivo: " + path, ex);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
