from flask import Flask, request, jsonify

import test
import py_eureka_client.eureka_client as eureka_client

rest_port = 5000
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                    app_name="ai-service",
                    instance_host="localhost",
                    instance_port=rest_port)

app = Flask(__name__)

# health check
@app.route('/ai/health', methods=['GET'])
def ai_server_health():
    return "AI server is running."


@app.route('/ai/run', methods=['POST'])
def ai_server_run():
    file_name = request.args.get('file_name')
    if file_name is None:
        return jsonify({"error": "'file_name' parameter is missing."}), 400

    result = test.main(file_name)
    return jsonify(result)

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=rest_port, debug=True)