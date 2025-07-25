package hr.lknezevic.entitygen.enums;

public enum FetchType {
    EAGER, // odmah se dohvaća zajedno s entitetom
    LAZY   // dohvaća se na zahtjev (kada se eksplicitno koristi)
}
