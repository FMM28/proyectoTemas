from flask import Flask,jsonify,send_file,request,render_template,make_response
from flask_jwt_extended import JWTManager, jwt_required
from dotenv import load_dotenv
from os import getenv
from openpyxl import Workbook
from io import BytesIO
from datetime import datetime
from weasyprint import HTML

load_dotenv()
app = Flask(__name__)
app.config['JWT_SECRET_KEY'] = getenv('FLASK_JWT')
app.config["JWT_ALGORITHM"] = "HS256"
jwt = JWTManager(app)

@app.route('/api/generar_excel', methods=['POST', 'GET'])
@jwt_required()
def generar_excel():
    try:
        if not request.is_json:
            return jsonify({"error": "Se requiere JSON"}), 400

        data = request.get_json()

        if not isinstance(data, list) or not all(isinstance(row, dict) for row in data):
            return jsonify({"error": "Se espera una lista de objetos JSON"}), 400

        columns = [
            'id', 'nombre', 'precio', 'stock',
            'categoria', 'razonSocialProveedor', 'rfc', 'correo'
        ]

        wb = Workbook()
        ws = wb.active
        ws.append(columns)

        for item in data:
            row = [item.get(col, "") for col in columns]
            ws.append(row)

        excel_buffer = BytesIO()
        wb.save(excel_buffer)
        excel_buffer.seek(0)

        return send_file(
            excel_buffer,
            mimetype='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            as_attachment=True,
            download_name=f'inventario_{datetime.now().strftime('%Y-%m-%d_%H-%M-%S')}.xlsx'
        )

    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/api/generar_recibo', methods=['POST', 'GET'])
@jwt_required()
def generar_recibo():

    recibo = request.get_json()
        
    if not recibo:
        return {"error": "No se proporcionó JSON"}, 400
    
    required_fields = ["id", "vendedor", "fecha", "cliente", "productos", "total"]
    for field in required_fields:
        if field not in recibo:
            return {"error": f"Campo obligatorio faltante: {field}"}, 400
    
    try:
        html = render_template('recibo.html', recibo=recibo)

        base_url = request.url_root

        pdf_buffer = BytesIO()

        HTML(string=html, base_url=base_url).write_pdf(pdf_buffer)

        pdf_buffer.seek(0)

        response = make_response(pdf_buffer.read())
        response.headers['Content-Type'] = 'application/pdf'
        response.headers['Content-Disposition'] = f'attachment; filename=recibo_{recibo["id"]}.pdf'

        return response
        
    except Exception as e:
        return {"error": f"Error al generar el recibo: {str(e)}"}, 500


app.run('localhost',8888,debug=False)