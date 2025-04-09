package org.example.modelo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor


public class JoyaVO {


    private int joya_id ;
    private String nombre ;
    private String material;
    private double peso;
    private double precio;
    private int stock;
}
