import { calcularPago } from "../domain/pagos.js";

export function crearControlPago({
    montoInput,
    metodoInput,
    montoAbonadoElemento,
    cambioElemento,
    filaCambio,
    errorElemento,
    obtenerSaldo
}) {
    function calcular() {
        return calcularPago(
            montoInput.value,
            obtenerSaldo(),
            metodoInput.value
        );
    }

    function actualizar() {
        try {
            const { pago, cambio } = calcular();

            montoAbonadoElemento.textContent = pago.monto_abonado.toFixed(2);
            cambioElemento.textContent = cambio.toFixed(2);
            filaCambio.hidden = pago.metodo !== "EFECTIVO";
            errorElemento.textContent = "";
        } catch (error) {
            montoAbonadoElemento.textContent = "0.00";
            cambioElemento.textContent = "0.00";
            filaCambio.hidden = metodoInput.value !== "EFECTIVO";
            errorElemento.textContent = montoInput.value ? error.message : "";
        }
    }

    montoInput.addEventListener("input", actualizar);
    metodoInput.addEventListener("change", actualizar);
    actualizar();

    return {
        actualizar,
        obtenerPago() {
            return calcular().pago;
        }
    };
}
