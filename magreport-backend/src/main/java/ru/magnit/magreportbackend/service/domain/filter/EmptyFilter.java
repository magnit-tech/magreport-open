package ru.magnit.magreportbackend.service.domain.filter;

public class EmptyFilter implements CubeFilterNode{
    @Override
    public boolean filter(int row) {
        return true;
    }
}
