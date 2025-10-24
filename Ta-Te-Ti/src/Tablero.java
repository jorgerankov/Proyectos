public class Tablero {

    String L1 = " ";
    String L2 = " ";
    String L3 = " ";

    String M1 = " ";
    String M2 = " ";
    String M3 = " ";

    String D1 = " ";
    String D2 = " ";
    String D3 = " ";

    public String getL1() {
        return L1;
    }

    public void setL1(String l1) {
        L1 = l1;
    }

    public String getL2() {
        return L2;
    }

    public void setL2(String l2) {
        L2 = l2;
    }

    public String getL3() {
        return L3;
    }

    public void setL3(String l3) {
        L3 = l3;
    }

    public String getM1() {
        return M1;
    }

    public void setM1(String m1) {
        M1 = m1;
    }

    public String getM2() {
        return M2;
    }

    public void setM2(String m2) {
        M2 = m2;
    }

    public String getM3() {
        return M3;
    }

    public void setM3(String m3) {
        M3 = m3;
    }

    public String getD1() {
        return D1;
    }

    public void setD1(String d1) {
        D1 = d1;
    }

    public String getD2() {
        return D2;
    }

    public void setD2(String d2) {
        D2 = d2;
    }

    public String getD3() {
        return D3;
    }

    public void setD3(String d3) {
        D3 = d3;
    }

    public void printTablero() {
        System.out.println("|" + L1 + "|" + L2 + "|" + L3 + "|" + "\n"
            + "|" + M1 + "|" + M2 + "|" + M3 + "|" + "\n" +
            "|" + D1 + "|" + D2 + "|" + D3 + "|");
    }

    public boolean vacio(String pos){
        if (null != pos) switch (pos) {
                case "L1" -> {
                    return " ".equals(this.getL1());
            }
                case "L2" -> {
                    return " ".equals(this.getL2());
            }
                case "L3" -> {
                    return " ".equals(this.getL3());
            }
                case "M1" -> {
                    return " ".equals(this.getM1());
            }
                case "M2" -> {
                    return " ".equals(this.getM2());
            }
                case "M3" -> {
                    return " ".equals(this.getM3());
            }
                case "D1" -> {
                    return " ".equals(this.getD1());
            }
                case "D2" -> {
                    return " ".equals(this.getD2());
            }
                case "D3" -> {
                    return " ".equals(this.getD3());
            }

        }
            return true;
    }

    public void setearPieza(String pos, String valor) {
        if (null != pos) switch (pos) {
            case "L1" -> {
                    this.setL1(valor);
            }
                case "L2" -> {
                    this.setL2(valor);
            }
                case "L3" -> {
                    this.setL3(valor);
            }
                case "M1" -> {
                    this.setM1(valor);
            }
                case "M2" -> {
                    this.setM2(valor);
            }
                case "M3" -> {
                    this.setM3(valor);
            }
                case "D1" -> {
                    this.setD1(valor);
            }
                case "D2" -> {
                    this.setD2(valor);
            }
                case "D3" -> {
                    this.setD3(valor);
            }

        }
    }

    public boolean posValida(String pos) {
        return "L1".equals(pos) || "L2".equals(pos) || "L3".equals(pos) ||
               "M1".equals(pos) || "M2".equals(pos) || "M3".equals(pos) ||
               "D1".equals(pos) || "D2".equals(pos) || "D3".equals(pos);
    }

}
