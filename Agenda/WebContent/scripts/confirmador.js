/**
 * Confirmador de exclusão
 */
function confirmar(id){
	let resposta = confirm("Confirma a exclusão?")
	if (resposta === true) {
		window.location.href = "delete?id=" + id
	}
}