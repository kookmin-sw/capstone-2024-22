from flask import Flask, request, jsonify
import os
import test
import py_eureka_client.eureka_client as eureka_client


rest_port = 5000
instance_ip = "15.164.139.204"
eureka_client.init(eureka_server="http://wasuphj.synology.me:8761/eureka",
                    app_name="ai-service",
                    instance_host=instance_ip,
                    instance_port=rest_port)

app = Flask(__name__)

# health check
@app.route('/ai/health', methods=['GET'])
def ai_server_health():
    return "AI server is running."


@app.route('/ai/run', methods=['POST'])
def ai_server_run():
    file_name = request.json.get('file_name')  # 요청의 JSON 본문에서 'file_name' 가져오기
    file_path = request.json.get('file_path')  # 요청의 JSON 본문에서 'file_path' 가져오기
    if file_name is None:
        return jsonify({"error": "'file_name' parameter is missing."}), 400

    # # for naive demo, for real demo delete output and change result
    # output = dict()
    # output["text"] = "안녕하세요"
    # output["emotions"] = {"sad": 75.3, "happy": 4.7, "angry": 7.1, "neutral": 12.9}
    # output["status"] = "200"
    # output["error"] = None
    # output["file_name"] = file_name
    # output["file_path"] = file_path
    # return jsonify(output)
    
    result = test.main(file_name=file_name, file_path=file_path)
    return jsonify(result)

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=rest_port, debug=True)