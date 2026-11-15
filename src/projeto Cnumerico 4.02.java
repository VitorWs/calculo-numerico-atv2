public class AreaRio{

     static double trapezioRepetido(double[] f, double h){
        int n = f.length - 1;
        double soma = f[0] + f[n];
        for(int i = 1; i < n; i++){
            soma += 2 * f[i];
        }
        return (h/2) * soma;
     }

     static double simpsonRepetido(double[] f, double h){
        int n =  f.length - 1;
        if(n % 2 != 0){
            throw new IllegalArgumentException("O número de subintervalos deve ser par.");
        }
        double soma = f[0] + f[n];
        for(int i = 1; i<n; i++){
            if(i % 2 == 0){
                soma += 2 * f[i];
            } else {
                soma += 4 * f[i];
            }
        }
        return (h/3) * soma;

     }

      public static void main(String[] args) {
        double[] m1 = {50.8, 86.2, 136, 72.8, 51};
        double[] m2 = {113.6, 144.5, 185, 171.2, 95.3};
        double h = 10;

        double[] f = new double[m1.length];
        for (int i = 0; i < f.length; i++) f[i] = m2[i] - m1[i];

        System.out.printf("Trapézio repetido: %.2f m²%n", trapezioRepetido(f, h));
        System.out.printf("Simpson repetido:  %.2f m²%n", simpsonRepetido(f, h));
    }
}
