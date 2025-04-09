package org.example.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor


public class ClienteVO {


private int cliente_id;
private  String nombre;
private String correo;
private String telefono;

}
