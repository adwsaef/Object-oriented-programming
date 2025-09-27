package market;

public class Company {
    private final String Id;
    private final int startPrice;

    public Company(String Id2, int previousPrice2) {
        if (Id2.isEmpty() || Id2.length() > 5)
            throw new RuntimeException("niepoprawna długość nazwy akcji: " + Id2);
        for (char a : Id2.toCharArray()) {
            if (!Character.isUpperCase(a))
                throw new RuntimeException("niepoprawny znak w nazawie firmy: " + Id2);
        }
        Id = Id2;

        if (previousPrice2 <= 0)
            throw new RuntimeException("niepoprawna cena ostatniej transakcji" + previousPrice2);

        startPrice = previousPrice2;
    }

    public String getId() {
        return Id;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public String toString() {
        return "Akcje: " + Id + ", cena początkowa: " + startPrice + "\n";
    }
}
