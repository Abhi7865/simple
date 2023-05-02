import openai
import gradio as gr
import requests
import json


url = "https://api.openai.com/v1/completions"

openai.api_key = "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"

headers = {'User-Agent': 'ceilometerclient.openstack.common.apiclient',
           'X-Auth-Token': 'A0007898393509957505790!adt6eTHtqQjWWShVg+wmKgLdsIvdV7eNJ59whybllS03n2d/k6NFvlatI8dCgEBtfakqMA4UxE38XEPBuwSY7KdnAyE='
           }
headers1 = {
    'Authorization': 'Bearer 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',
    'Content-Type': 'application/json'
}          
     
messages = [
    {"role": "system", "content": "You are a helpful and kind AI Assistant."},
]
def chatbot(input):
    if "account balance"  in input:
        res = requests.get("https://core-api.partner-shared-sandbox.tmachine.io/v1/balances/2348_4ae44aae-cb36-4f6a-8d30-f9f543f1eb1d",headers=headers)
        json_lineData= json.dumps(res.json())
        data = {
            "model": "text-davinci-003",
            "prompt": json_lineData+"| account balance",
            "temperature": 0,
            "max_tokens": 100,
            "top_p": 1,
            "frequency_penalty": 0,
            "presence_penalty": 0}
        response = requests.post(url, headers=headers1, json=data)
        print(response.status_code)
        jData= json.dumps(response.json())
        parsed_data = json.loads(jData)
        outp=parsed_data["choices"]
        reply=outp[0]
        output_data =reply["text"]
        return output_data
        
    elif "my account address" in input:
        print("address")
        res = requests.get("https://core-api.partner-shared-sandbox.tmachine.io/v1/customers/LTIM997",headers=headers)
        json_lineData= json.dumps(res.json())
        data = {
             "model": "text-davinci-003",
             "prompt": json_lineData+"| my account address",
             "temperature": 0,
             "max_tokens": 100,
             "top_p": 1,
             "frequency_penalty": 0,
             "presence_penalty": 0}
        response = requests.post(url, headers=headers1, json=data)
        print(response.status_code)
        #print(response.json())
        jData= json.dumps(response.json())
        parsed_data = json.loads(jData)
        outp=parsed_data["choices"]
        #print(outp)
        reply=outp[0]
        output_data =reply["text"]
        return output_data
        
    elif "settlement" in input:
        res = requests.get("https://core-api.partner-shared-sandbox.tmachine.io/v1/posting-instruction-batches?page_size=4&account_ids=LTIM43074690",headers=headers)
        json_lineData= json.dumps(res.json())
       # print(json_lineData)
        data = {
             "model": "text-davinci-003",
             "prompt": json_lineData+"|"+ input,
             "temperature": 0,
             "max_tokens": 100,
             "top_p": 1,
             "frequency_penalty": 0,
             "presence_penalty": 0}
        response = requests.post(url, headers=headers1, json=data)
        #print(response.json())
        print(response.status_code)
        jData= json.dumps(response.json())
        #print(jData)
        parsed_data = json.loads(jData)
        outp=parsed_data["choices"]
        reply=outp[0]
        output_data =reply["text"]
        return output_data
    
    elif "transaction" in input:    
          # open the json file
          with open('tranjson.json', 'r') as f:
             # load the data
            data1 = json.load(f)
         # convert the data to jsonlines
          jsonlines = json.dumps(data1, indent=4, sort_keys=True)  
          data = {
                "model": "text-davinci-003",
                "prompt":jsonlines+"|"+input,
                "temperature": 0,
                "max_tokens": 100,
                "top_p": 1,
                "frequency_penalty": 0,
                "presence_penalty": 0}
          response = requests.post(url, headers=headers1, json=data)
          print(response.status_code)
          #print(response.json())
          jData= json.dumps(response.json())
          parsed_data = json.loads(jData)
          outp=parsed_data["choices"]
          #print(outp)
          reply=outp[0]
          output_data =reply["text"]
          return output_data

        
    else:
     #messages.append({"role": "user", "content": input})
        chat = openai.ChatCompletion.create(
            model="gpt-3.5-turbo", messages=messages
        )
        reply = chat.choices[0].message.content
        messages.append({"role": "assistant", "content": reply})
        return reply

       

inputs = gr.inputs.Textbox(lines=7, label="Chat with AI")
outputs = gr.outputs.Textbox(label="Reply")

gr.Interface(fn=chatbot, inputs=inputs, outputs=outputs, title="AI Chatbot",
             description="Ask anything you want",
             theme="compact").launch(share=True)