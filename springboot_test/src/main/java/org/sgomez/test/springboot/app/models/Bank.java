package org.sgomez.test.springboot.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bank {
    private Long id;
    private String name;
    private int nTransferencias;
}
