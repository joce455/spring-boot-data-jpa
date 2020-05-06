package com.bolsaideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsaideas.springboot.app.entyties.Factura;
import com.bolsaideas.springboot.app.entyties.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
@author Jose Rodolfo Juarez
@version 1.0
@since  
*/

@Controller("factura/ver")
public class FacturaPdfView extends AbstractPdfView{

	@Autowired
	private MessageSource messaSource;
	
	@Autowired
	private LocaleResolver localeResolver;
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Factura factura= (Factura) model.get("factura");
		
		Locale locale= localeResolver.resolveLocale(request);
		
		PdfPTable tabla1= new PdfPTable(1);
		tabla1.setSpacingAfter(20);
		PdfPCell cellDatosCliente= new PdfPCell(new Phrase(messaSource.getMessage("text.cliente.detalle.titulo", null, locale)));
		cellDatosCliente.setBackgroundColor(new Color(184, 218, 255));
		cellDatosCliente.setPadding(8f);
		tabla1.addCell(cellDatosCliente);
		tabla1.addCell(factura.getCliente().getNombre()+' '+factura.getCliente().getApellido());
		tabla1.addCell(factura.getCliente().getEmail());
		
		
		PdfPTable tabla2= new PdfPTable(1);
		
		tabla2.setSpacingAfter(20);
		
		PdfPCell cellDatosFactura= new PdfPCell(new Phrase(messaSource.getMessage("text.factura.ver.datos.factura", null, locale)));
		cellDatosFactura.setBackgroundColor(new Color(195, 230, 203));
		cellDatosFactura.setPadding(8f);
		
		tabla2.addCell(cellDatosFactura);
		tabla2.addCell("Id : ".concat(String.valueOf(factura.getId())));
		tabla2.addCell(messaSource.getMessage("text.cliente.factura.descripcion", null, locale)
				.concat(" : "+factura.getDescripcion()));
		
		tabla2.addCell(messaSource.getMessage("text.cliente.factura.fecha", null, locale)
				.concat(" : "+String.valueOf(factura.getCreateAt())));
		
		PdfPTable tabla3= new PdfPTable(4);
		
		
		tabla3.setWidths(new float[] {2.5f,1,1,1});
		
		
		
		tabla3.addCell(messaSource.getMessage("text.factura.form.item.nombre", null, locale));
		tabla3.addCell(messaSource.getMessage("text.factura.form.item.precio", null, locale));
		tabla3.addCell(messaSource.getMessage("text.factura.form.item.cantidad", null, locale));
		tabla3.addCell(messaSource.getMessage("text.factura.form.item.total", null, locale));
		
		
		for (ItemFactura item :factura.getItems()) {
			tabla3.addCell(item.getProducto().getNombre());
			tabla3.addCell(String.valueOf(item.getProducto().getPrecio()));
			tabla3.addCell(String.valueOf(item.getCantidad()));
			tabla3.addCell(String.valueOf(item.calcularImporte()));
			
		}
		PdfPCell cell= new PdfPCell(new Phrase("Total: "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell(String.valueOf(factura.getTotal()));
		document.add(tabla1);
		document.add(tabla2);
		document.add(tabla3);
		
		
	}

}
