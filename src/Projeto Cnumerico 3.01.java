class LeiDeMoore {

    public static void main(String[] args) {
        double[] ano = {1971, 1972, 1974, 1978, 1982, 1986, 1989, 1993, 1997, 1999, 2000};
        double[] N   = {2250, 3300, 6000, 29000, 134000, 275000, 1200000,
                        3100000, 7500000, 9500000, 42000000};

        int m = ano.length;
        double[] y = new double[m];
        for (int i = 0; i < m; i++) y[i] = Math.log10(N[i]); // transformação

        // Eq. normais: y = a*x + c
        double sx = 0, sy = 0, sxx = 0, sxy = 0;
        for (int i = 0; i < m; i++) {
            sx  += ano[i];
            sy  += y[i];
            sxx += ano[i] * ano[i];
            sxy += ano[i] * y[i];
        }
        double a = (m * sxy - sx * sy) / (m * sxx - sx * sx);
        double c = (sy - a * sx) / m;

        System.out.printf("log10(N) = %.5f * ano + (%.5f)%n", a, c);
        System.out.printf("Dobra a cada %.2f anos%n", Math.log10(2) / a);

        for (int anoPrev : new int[]{2010, 2020}) {
            double logN = a * anoPrev + c;
            System.out.printf("Previsão %d: %.3e transistores%n", anoPrev, Math.pow(10, logN));
        }
    }
}