let graph = {
  "nodes": [
    //病人
    {"id": "another patient", "group": 1},
    {"id": "a patient", "group": 1},
    {"id": "male", "group": 6},
    {"id": "female", "group": 6},
    //疾病种类
    {"id": "Animal_bacterial_diseases", "group": 4},
    {"id": "Clostridial_infections", "group": 4},
    //住院记录
    {"id": "100001", "group": 5},
    {"id": "2007/9/11 11:46:00", "group": 7},
    {"id": "1:29:00", "group": 7},
    {"id": "100003", "group": 5},
    {"id": "2010/4/17 15:34:00", "group": 7},
    {"id": "9:30:00", "group": 7},
    {"id": "100006", "group": 5},
    {"id": "2008/4/6 15:49:00", "group": 7},
    {"id": "4:59:00", "group": 7},
    {"id": "100007", "group": 5},
    {"id": "2015/3/31 5:33:00", "group": 7},
    {"id": "7:07:00", "group": 7},
    //疾病
    {"id": "Tetanus", "group": 2},
    {"id": "Mth sus Stph aur els/NOS", "group": 2},
    {"id": "Sec mal neo brain/spine", "group": 2},
    {"id": "Second malig neo liver", "group": 2},
    {"id": "Secondary malig neo bone", "group": 2},
    {"id": "Listeriosis", "group": 2},
    //药物
    {"id": "Heparin", "group": 3},
    {"id": "5000unit", "group": 0},
    {"id": "Insulin", "group": 3},
    {"id": "992", "group": 0},
    {"id": "1.98%", "group": 0},
    {"id": "Sodium Chloride 0.9%  Flush", "group": 3},
    {"id": "3ml", "group": 0},
    {"id": "Acetaminophen", "group": 3},
    {"id": "650mg", "group": 0}
  ],
  "links": [
    {"source": "Animal_bacterial_diseases", "target": "Listeriosis", "value": 1},
    {"source": "1.98%", "target": "992", "value": 1},
    {"source": "Insulin", "target": "992", "value": 1},
    {"source": "Insulin", "target": "1.98%", "value": 1},
    {"source": "Mth sus Stph aur els/NOS", "target": "100001", "value": 1},
    {"source": "Mth sus Stph aur els/NOS", "target": "Insulin", "value": 1},
    {"source": "Mth sus Stph aur els/NOS", "target": "Heparin", "value": 1},
    {"source": "5000unit", "target": "Heparin", "value": 1},
    {"source": "2015/3/31 5:33:00", "target": "100007", "value": 1},
    {"source": "100007", "target": "7:07:00", "value": 1},
    {"source": "100007", "target": "Secondary malig neo bone", "value": 1},
    {"source": "Heparin", "target": "Secondary malig neo bone", "value": 1},
    {"source": "Acetaminophen", "target": "Secondary malig neo bone", "value": 1},
    {"source": "Acetaminophen", "target": "650mg", "value": 1},
    {"source": "4:59:00", "target": "100001", "value": 1},
    {"source": "100003", "target": "2010/4/17 15:34:00", "value": 1},
    {"source": "100003", "target": "9:30:00", "value": 1},
    {"source": "a patient", "target": "100001", "value": 1},
    {"source": "a patient", "target": "100003", "value": 1},
    {"source": "a patient", "target": "male", "value": 1},
    {"source": "2007/9/11 11:46:00", "target": "100001", "value": 1},
    {"source": "100006", "target": "another patient", "value": 1},
    {"source": "100006", "target": "Sec mal neo brain/spine", "value": 1},
    {"source": "100006", "target": "Tetanus", "value": 1},
    {"source": "100006", "target": "2008/4/6 15:49:00", "value": 1},
    {"source": "100006", "target": "1:29:00", "value": 1},
    {"source": "Sec mal neo brain/spine", "target": "100003", "value": 1},
    {"source": "Sec mal neo brain/spine", "target": "Heparin", "value": 1},
    {"source": "Sec mal neo brain/spine", "target": "Insulin", "value": 1},
    {"source": "Sec mal neo brain/spine", "target": "Sodium Chloride 0.9%  Flush", "value": 1},
    {"source": "3ml", "target": "Sodium Chloride 0.9%  Flush", "value": 1},
    {"source": "another patient", "target": "female", "value": 1},
    {"source": "another patient", "target": "100007", "value": 1},
    {"source": "Tetanus", "target": "100003", "value": 1},
    {"source": "Tetanus", "target": "Heparin", "value": 1},
    {"source": "Tetanus", "target": "Clostridial_infections", "value": 1},
    {"source": "Tetanus", "target": "Animal_bacterial_diseases", "value": 1},
    {"source": "Second malig neo liver", "target": "100003", "value": 1},
    {"source": "Second malig neo liver", "target": "Heparin", "value": 1},
    {"source": "Second malig neo liver", "target": "Sodium Chloride 0.9%  Flush", "value": 1}
  ]
};

export default {
  graph: function () {
    return graph;
  }
}
