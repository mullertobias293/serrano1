/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoserrano;


import java.awt.Color;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class VentanaPrincipal {
    
    // Listas, colas y mapas para gestionar los productos, ventas y otras acciones
    private static List<Producto> inventario = new ArrayList<>();
    private static Queue<Venta> historialVentas = new LinkedList<>();
    private static Map<String, Producto> productosMap = new HashMap<>();
    
    public static void main(String[] args) {
        
        // Crear la ventana principal
        JFrame frame = new JFrame("Sistema de Ventas Manaos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.RED);

        // Configurar imagen
        ImageIcon icon = new ImageIcon(VentanaPrincipal.class.getResource("/images/logo.png"));
        Image scaledImage = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        JLabel labelImagen = new JLabel(resizedIcon);
        labelImagen.setBounds(175, 10, 300, 175);
        frame.add(labelImagen);

        // Botones
        JButton btnInventario = new JButton("Ver Inventario");
        btnInventario.setBounds(50, 250, 150, 30);
        frame.add(btnInventario);

        JButton btnVenta = new JButton("Registrar Venta");
        btnVenta.setBounds(50, 300, 150, 30);
        frame.add(btnVenta);

        JButton btnAgregarBebida = new JButton("Agregar Bebida");
        btnAgregarBebida.setBounds(220, 250, 150, 30);
        frame.add(btnAgregarBebida);

        JButton btnHistorial = new JButton("Historial de Ventas");
        btnHistorial.setBounds(220, 300, 150, 30);
        frame.add(btnHistorial);

        JButton btnEliminarBebida = new JButton("Eliminar Bebida");
        btnEliminarBebida.setBounds(390, 250, 150, 30);
        frame.add(btnEliminarBebida);

        JButton btnActualizarStock = new JButton("Actualizar Stock");
        btnActualizarStock.setBounds(390, 300, 150, 30);
        frame.add(btnActualizarStock);

        // TextArea para mostrar mensajes
        JTextArea textArea = new JTextArea();
        textArea.setBounds(50, 350, 600, 200);
        textArea.setEditable(false);
        frame.add(textArea);

        //"Ver Inventario"
        btnInventario.addActionListener(e -> {
            StringBuilder inventarioStr = new StringBuilder("Inventario:\n");
            for (Producto p : inventario) {
                inventarioStr.append("Nombre: ").append(p.getNombre())
                             .append(", Precio: ").append(p.getPrecio())
                             .append(", Stock: ").append(p.getStock())
                             .append("\n");
            }
            textArea.setText(inventarioStr.toString());
        });

        //"Registrar Venta"
        btnVenta.addActionListener(e -> {
            String nombreProducto = JOptionPane.showInputDialog(frame, "Ingrese el nombre del Producto:");
            String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese Cantidad:");
            try {
                if (nombreProducto != null && !nombreProducto.isEmpty() && cantidadStr != null && !cantidadStr.isEmpty()) {
                    int cantidad = Integer.parseInt(cantidadStr);
                    Producto producto = productosMap.get(nombreProducto);
                    if (producto != null && producto.getStock() >= cantidad) {
                        producto.setStock(producto.getStock() - cantidad);
                        Venta venta = new Venta(producto, cantidad);
                        historialVentas.add(venta);
                        textArea.setText("Venta realizada con éxito.");
                    } else {
                        textArea.setText("Stock insuficiente o producto no encontrado.");
                    }
                } else {
                    textArea.setText("Todos los campos son obligatorios.");
                }
            } catch (NumberFormatException ex) {
                textArea.setText("Entrada inválida.");
            }
        });

        // "Agregar Bebida"
        btnAgregarBebida.addActionListener(e -> {
    try {
        String nombre = JOptionPane.showInputDialog(frame, "Ingrese el nombre de la bebida:");
        String precioStr = JOptionPane.showInputDialog(frame, "Ingrese el precio de la bebida:");
        String stockStr = JOptionPane.showInputDialog(frame, "Ingrese el stock inicial:");

        if (nombre != null && !nombre.isEmpty() && precioStr != null && stockStr != null) {
            // Verificar que el nombre comienza con "Manaos"
            if (!nombre.toLowerCase().startsWith("manaos")) {
                textArea.setText("El nombre de la bebida debe comenzar con 'Manaos'.");
                return; // Detener la ejecución si el nombre no es válido
            }

            // Convertir precio y stock a sus tipos correspondientes
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            // Crear un nuevo producto y agregarlo al inventario y al mapa
            Producto nuevoProducto = new Producto(nombre, precio, stock);
            inventario.add(nuevoProducto);
            productosMap.put(nombre, nuevoProducto);
            
            // Mostrar mensaje de éxito
            textArea.setText("Bebida agregada con éxito.");
        } else {
            textArea.setText("Todos los campos son obligatorios.");
        }
    } catch (NumberFormatException ex) {
        textArea.setText("Entrada inválida. Verifique los datos ingresados.");
    }
});


        //"Eliminar Bebida"
        btnEliminarBebida.addActionListener(e -> {
    String nombre = JOptionPane.showInputDialog(frame, "Ingrese el nombre de la bebida a eliminar:");
    if (nombre != null && !nombre.isEmpty()) {
        // Verificar si la bebida existe en el mapa
        Producto producto = productosMap.get(nombre);
        if (producto != null) {
            // Preguntar al usuario si está seguro de que desea eliminar la bebida
            int respuesta = JOptionPane.showConfirmDialog(frame,
                    "¿Está seguro que desea eliminar la bebida '" + nombre + "'?",
                    "Confirmación de eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            // Si el usuario confirma la eliminación
            if (respuesta == JOptionPane.YES_OPTION) {
                productosMap.remove(nombre);
                inventario.remove(producto);
                textArea.setText("Bebida eliminada con éxito.");
            } else {
                textArea.setText("Eliminación cancelada.");
            }
        } else {
            textArea.setText("Bebida no encontrada.");
        }
    } else {
        textArea.setText("El nombre de la bebida es obligatorio.");
    }
});

        //"Actualizar Stock"
        btnActualizarStock.addActionListener(e -> {
            try {
                String nombreBebida = JOptionPane.showInputDialog(frame, "Ingrese el nombre de la bebida:");
                String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad de stock a agregar:");
                if (nombreBebida != null && !nombreBebida.isEmpty() && cantidadStr != null && !cantidadStr.isEmpty()) {
                    int cantidad = Integer.parseInt(cantidadStr);
                    Producto producto = productosMap.get(nombreBebida);
                    if (producto != null) {
                        producto.setStock(producto.getStock() + cantidad);
                        textArea.setText("Stock actualizado correctamente.");
                    } else {
                        textArea.setText("Bebida no encontrada.");
                    }
                } else {
                    textArea.setText("Todos los campos son obligatorios.");
                }
            } catch (NumberFormatException ex) {
                textArea.setText("Entrada inválida. Verifique los datos ingresados.");
            }
        });

        //"Historial de Ventas"
        btnHistorial.addActionListener(e -> {
            StringBuilder historialStr = new StringBuilder("Historial de Ventas:\n");
            for (Venta v : historialVentas) {
                historialStr.append("Producto: ").append(v.getProducto().getNombre())
                             .append(", Cantidad: ").append(v.getCantidad())
                             .append(", Total: ").append(v.getTotal())
                             .append("\n");
            }
            textArea.setText(historialStr.toString());
        });

        // Mostrar ventana
        frame.setVisible(true);
    }

    // Clase Producto
    public static class Producto {
        private String nombre;
        private double precio;
        private int stock;

        public Producto(String nombre, double precio, int stock) {
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }

        public String getNombre() {
            return nombre;
        }

        public double getPrecio() {
            return precio;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }

    // Clase Venta
    public static class Venta {
        private Producto producto;
        private int cantidad;

        public Venta(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public Producto getProducto() {
            return producto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public double getTotal() {
            return producto.getPrecio() * cantidad;
        }
    }
}


