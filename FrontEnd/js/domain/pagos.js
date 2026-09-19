function redondearDinero(valor) {
    return Math.round((valor + Number.EPSILON) * 100) / 100;
}

export function calcularPago(montoRecibido, saldoPendiente, metodo) {
    const recibido = Number(montoRecibido);
    const saldo = Number(saldoPendiente);
    const metodoNormalizado = String(metodo ?? "").trim().toUpperCase();

    if (!Number.isFinite(saldo) || saldo <= 0) {
        throw new Error("El saldo pendiente debe ser mayor a cero");
    }

    if (!Number.isFinite(recibido) || recibido <= 0) {
        throw new Error("Ingresa un monto recibido mayor a cero");
    }

    if (!metodoNormalizado) {
        throw new Error("Selecciona un metodo de pago");
    }

    if (!["EFECTIVO", "TARJETA", "TRANSFERENCIA"].includes(metodoNormalizado)) {
        throw new Error("El metodo de pago no es valido");
    }

    if (metodoNormalizado !== "EFECTIVO" && recibido > saldo) {
        throw new Error("En tarjeta o transferencia el monto no puede superar el saldo");
    }

    const montoAbonado = redondearDinero(Math.min(recibido, saldo));
    const cambio = metodoNormalizado === "EFECTIVO"
        ? redondearDinero(Math.max(recibido - saldo, 0))
        : 0;

    return {
        pago: {
            monto_abonado: montoAbonado,
            monto_recibido: redondearDinero(recibido),
            metodo: metodoNormalizado
        },
        cambio
    };
}
