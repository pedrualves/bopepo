import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;
import org.jrimum.utilix.ClassLoaders;
import org.jrimum.utilix.text.DateFormat;

public class MeuPrimeiroBoletoItauCarteira {

	public static void main(String[] args) {

		Cedente cedente = new Cedente(null);

		Sacado sacado = new Sacado(null);

		Endereco enderecoSac = new Endereco();
		enderecoSac.setUF(UnidadeFederativa.AL);
		enderecoSac.setLocalidade("Testando");
		enderecoSac.setCep(new CEP("12345-678"));
		enderecoSac.setBairro("Praia dos testes");
		enderecoSac.setLogradouro("mais um endereço de teste");
		enderecoSac.setNumero("123");
		sacado.addEndereco(enderecoSac);
		
		SacadorAvalista sacadorAvalista = new SacadorAvalista("1234567890 - Teste");

		Endereco enderecoSacAval = new Endereco();
		enderecoSacAval.setUF(UnidadeFederativa.SP);
		enderecoSacAval.setLocalidade("São Paulo");
		enderecoSacAval.setCep(new CEP("00000-000"));
		enderecoSacAval.setBairro("Bairro teste");
		enderecoSacAval.setLogradouro("Rua dos testes");
		enderecoSacAval.setNumero("1");
		sacadorAvalista.addEndereco(enderecoSacAval);

		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_ITAU.create());
		contaBancaria.setNumeroDaConta(new NumeroDaConta(12345, "6"));
		contaBancaria.setCarteira(new Carteira(175));
		contaBancaria.setAgencia(new Agencia(1234));
		
		Titulo titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
		titulo.setNumeroDoDocumento("87654321");
		titulo.setNossoNumero("87654321");
		titulo.setValor(BigDecimal.valueOf(987.65));
		
		titulo.setDataDoDocumento(DateFormat.DDMMYYYY_B.parse("03/06/2013"));
		titulo.setDataDoVencimento(DateFormat.DDMMYYYY_B.parse("06/06/2013"));
		
		titulo.setTipoDeDocumento(TipoDeTitulo.RC_RECIBO);
		titulo.setAceite(Aceite.N);
		//titulo.setDesconto(new BigDecimal(0.05));
		//titulo.setDeducao(BigDecimal.ZERO);
		//titulo.setMora(BigDecimal.ZERO);
		//titulo.setAcrecimo(BigDecimal.ZERO);
		//titulo.setValorCobrado(BigDecimal.ZERO);

		Boleto boleto = new Boleto(titulo);
		
		boleto.setLocalPagamento("local");
		boleto.setInstrucaoAoSacado("Olha a fatura...");
		boleto.setInstrucao1("Fatura...");


		File templatePersonalizado = new File(ClassLoaders.getResource("BoletoTemplateComSacadorAvalista.pdf").getFile());

		BoletoViewer boletoViewer = new BoletoViewer(boleto,templatePersonalizado);
		
		File arquivoPdf = boletoViewer.getPdfAsFile("MeuPrimeiroBoleto.pdf");

		mostreBoletoNaTela(arquivoPdf);
	}
 
	private static void mostreBoletoNaTela(File arquivoBoleto) {

		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
		
		try {
			desktop.open(arquivoBoleto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
