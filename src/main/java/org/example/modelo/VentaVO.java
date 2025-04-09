package org.example.modelo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class VentaVO {

    private int venta_id;
    private int cliente_id;
    private int joya_id;
    private int cantidad;
    private LocalDate fecha;

}
