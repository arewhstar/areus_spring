package com.example.demo.Mappers;

public interface Mapper<A,B>{

    B mapTo(A a);

    A mapFrom(B b);
}
