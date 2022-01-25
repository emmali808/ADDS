#https://dexur.com/icd9/ code website
"""
ALL
"""

class Voc(object):
    def __init__(self):
        self.idx2word = {}
        self.word2idx = {}
    
    def add_sentence(self,sentence):
        for word in sentence:
            if word not in self.word2idx:
                self.idx2word[len(self.word2idx)] = word
                self.word2idx[word] = len(self.word2idx)


def _remove_duplicate(input):
    return list(set(input))


def build_stage_one_edges(res, graph_voc):
    """
    :param res:
    :param graph_voc:
    :return: edge_idx [[1,2,3],[0,1,0]]
    """
    edge_idx = []
    for sample in res:
        sample_idx = list(map(lambda x: graph_voc.word2idx[x], sample))
        for i in range(len(sample_idx) - 1):
            # only direct children -> ancestor
            edge_idx.append((sample_idx[i+1], sample_idx[i]))
            #
            # # self-loop except leaf node
            # if i != 0:
            #     edge_idx.append((sample_idx[i], sample_idx[i]))

    edge_idx = _remove_duplicate(edge_idx)
    row = list(map(lambda x: x[0], edge_idx))
    col = list(map(lambda x: x[1], edge_idx))
    return [row, col]


def build_stage_two_edges(res, graph_voc):
    """
    :param res:
    :param graph_voc:
    :return: edge_idx [[1,2,3],[0,1,0]]
    """
    edge_idx = []
    for sample in res:
        sample_idx = list(map(lambda x: graph_voc.word2idx[x], sample))
        # only ancestors -> leaf node
        edge_idx.extend([(sample_idx[0], sample_idx[i])
                         for i in range(1, len(sample_idx))])

    edge_idx = _remove_duplicate(edge_idx)
    row = list(map(lambda x: x[0], edge_idx))
    col = list(map(lambda x: x[1], edge_idx))
    return [row, col]

def build_ancestors(res, graph_voc):
    """
    :param res:
    :param graph_voc:
    :return: ancestor_nodes: the ancestor for leave nodes;  left_nodes: the leave nodes
    """
    ancestor_nodes,leave_nodes = [],[]
    for sample in res:
        sample_idx = list(map(lambda x: graph_voc.word2idx[x], sample))
        # only ancestors -> leaf node
        ancestor_nodes.append([sample_idx[i] for i in range(0, len(sample_idx))])
        leave_nodes.append([sample_idx[0] for i in range(0, len(sample_idx))])
    return ancestor_nodes,leave_nodes


def build_cominbed_edges(res, graph_voc):
    """
    :param res:
    :param graph_voc:
    :return: edge_idx [[1,2,3],[0,1,0]]
    """
    edge_idx = []
    for sample in res:
        sample_idx = list(map(lambda x: graph_voc.word2idx[x], sample))
        for i in range(len(sample_idx) - 1):
            # ancestor <- direct children
            edge_idx.append((sample_idx[i+1], sample_idx[i]))

            # ancestors -> leaf node
            edge_idx.extend([(sample_idx[0], sample_idx[i])
                             for i in range(1, len(sample_idx))])
            #
            #
            # # self-loop except leaf node
            # if i != 0:
            #     edge_idx.append((sample_idx[i], sample_idx[i]))

    edge_idx = _remove_duplicate(edge_idx)
    row = list(map(lambda x: x[0], edge_idx))
    col = list(map(lambda x: x[1], edge_idx))
    return [row, col]


# tree order


"""
ICD-9
"""


def expand_level2_diag():
    level2 = ['001-009', '010-018', '020-027', '030-041', '042', '045-049', '050-059', '060-066', '070-079', '080-088',
              '090-099', '100-104', '110-118', '120-129', '130-136', '137-139', '140-149', '150-159', '160-165',
              '170-176',
              '176', '179-189', '190-199', '200-208', '209', '210-229', '230-234', '235-238', '239', '240-246',
              '249-259',
              '260-269', '270-279', '280-289', '290-294', '295-299', '300-316', '317-319', '320-327', '330-337', '338',
              '339', '340-349', '350-359', '360-379', '380-389', '390-392', '393-398', '401-405', '410-414', '415-417',
              '420-429', '430-438', '440-449', '451-459', '460-466', '470-478', '480-488', '490-496', '500-508',
              '510-519',
              '520-529', '530-539', '540-543', '550-553', '555-558', '560-569', '570-579', '580-589', '590-599',
              '600-608',
              '610-613', '614-616', '617-629', '630-639', '640-649', '650-659', '660-669', '670-677', '678-679',
              '680-686',
              '690-698', '700-709', '710-719', '720-724', '725-729', '730-739', '740-759', '760-763', '764-779',
              '780-789',
              '790-796', '797-799', '800-804', '805-809', '810-819', '820-829', '830-839', '840-848', '850-854',
              '860-869',
              '870-879', '880-887', '890-897', '900-904', '905-909', '910-919', '920-924', '925-929', '930-939',
              '940-949',
              '950-957', '958-959', '960-979', '980-989', '990-995', '996-999', 'V01-V91', 'V01-V09', 'V10-V199',
              'V200-V299',
              'V300-V399', 'V400-V499', 'V500-V599', 'V600-V699', 'V700-V829', 'V830-V849', 'V850-V859', 'V860-V869', 'V870-V879', 'V880-V889', 'V890-V899',
              'V900-V909',
              'V910-V919', 'E000-E899', 'E000', 'E001-E030', 'E800-E807', 'E810-E819', 'E820-E825', 'E826-E829', 'E830-E838',
              'E840-E845', 'E846-E849', 'E850-E858', 'E860-E869', 'E870-E876', 'E878-E879', 'E880-E888', 'E890-E899',
              'E900-E909', 'E910-E915', 'E916-E928', 'E929', 'E930-E949', 'E950-E959', 'E960-E969', 'E970-E978',
              'E980-E989', 'E990-E999', 'Nodx', 'NoD'

              ]
    # level2 = ['001-009', '010-018', '020-027', '030-041', '042', '045-049', '050-059', '060-066', '070-079', '080-088',
    #           '090-099', '100-104', '110-118', '120-129', '130-136', '137-139', '140-149', '150-159', '160-165',
    #           '170-176',
    #           '176', '179-189', '190-199', '200-208', '209', '210-229', '230-234', '235-238', '239', '240-246',
    #           '249-259',
    #           '260-269', '270-279', '280-289', '290-294', '295-299', '300-316', '317-319', '320-327', '330-337', '338',
    #           '339', '340-349', '350-359', '360-379', '380-389', '390-392', '393-398', '401-405', '410-414', '415-417',
    #           '420-429', '430-438', '440-449', '451-459', '460-466', '470-478', '480-488', '490-496', '500-508',
    #           '510-519',
    #           '520-529', '530-539', '540-543', '550-553', '555-558', '560-569', '570-579', '580-589', '590-599',
    #           '600-608',
    #           '610-613', '614-616', '617-629', '630-639', '640-649', '650-659', '660-669', '670-677', '678-679',
    #           '680-686',
    #           '690-698', '700-709', '710-719', '720-724', '725-729', '730-739', '740-759', '760-763', '764-779',
    #           '780-789',
    #           '790-796', '797-799', '800-804', '805-809', '810-819', '820-829', '830-839', '840-848', '850-854',
    #           '860-869',
    #           '870-879', '880-887', '890-897', '900-904', '905-909', '910-919', '920-924', '925-929', '930-939',
    #           '940-949',
    #           '950-957', '958-959', '960-979', '980-989', '990-995', '996-999', 'V01-V91', 'V01-V09', 'V10-V19',
    #           'V20-V29',
    #           'V30-V39', 'V40-V49', 'V50-V59', 'V60-V69', 'V70-V82', 'V83-V84', 'V85', 'V86', 'V87', 'V88', 'V89',
    #           'V90',
    #           'V91', 'E000-E899', 'E000', 'E001-E030', 'E800-E807', 'E810-E819', 'E820-E825', 'E826-E829', 'E830-E838',
    #           'E840-E845', 'E846-E849', 'E850-E858', 'E860-E869', 'E870-E876', 'E878-E879', 'E880-E888', 'E890-E899',
    #           'E900-E909', 'E910-E915', 'E916-E928', 'E929', 'E930-E949', 'E950-E959', 'E960-E969', 'E970-E978',
    #           'E980-E989', 'E990-E999', 'Nodx', 'NoD'

    #           ]

    level2_expand = {}
    for i in level2:
        tokens = i.split('-')
        if i[0] == 'V':
            if len(tokens) == 1:
                level2_expand[i] = i
            else:
                for j in range(int(tokens[0][1:]), int(tokens[1][1:]) + 1):
                    level2_expand["V%02d" % j] = i
        elif i[0] == 'E':
            if len(tokens) == 1:
                level2_expand[i] = i
            else:
                for j in range(int(tokens[0][1:]), int(tokens[1][1:]) + 1):
                    level2_expand["E%03d" % j] = i
        else:
            if len(tokens) == 1:
                level2_expand[i] = i
            else:
                for j in range(int(tokens[0]), int(tokens[1]) + 1):
                    level2_expand["%03d" % j] = i
    return level2_expand

# ontology raw source
# http://icd9.chrisendres.com/index.php?action=procslist
def expand_level2_proce():
    level2 = ['00','01-05','06-07','08-16','17','18-20','21-29','30-34','35-39','40-41','42-54','55-59','60-64','65-71','72-75','76-84','85-86','87-99']
    level2_expand = {}
    for i in level2:
        tokens = i.split('-')
        if len(tokens)==1:
            level2_expand[i] = i
        else:
            for j in range(int(tokens[0]),int(tokens[1])+1):
                level2_expand["%02d"%j] = i
    return level2_expand


def build_diag_tree(unique_codes):
    res = []
    graph_voc = Voc()
    unique_codes = [code[2:] for code in unique_codes]

    root_node = 'diag_root'
    level3_dict = expand_level2_diag()
    for code in unique_codes:
        level1 = code
        level2 = level1[:4] if level1[0] == 'E' else level1[:3]
        level3 = level3_dict[level2]
        level4 = root_node

        sample = [level1, level2, level3, level4]

        graph_voc.add_sentence(sample)
        res.append(sample)
    return res, graph_voc

def build_proce_tree(unique_codes):
    res = []
    graph_voc = Voc()
    unique_codes = [code[2:] for code in unique_codes]

    root_node = 'proce_root'
    level3_dict = expand_level2_proce()
    for code in unique_codes:
        level1 = code
        level2 = level1[:2]
        level3 = level3_dict[level2]
        level4 = root_node

        sample = [level1, level2, level3, level4]

        graph_voc.add_sentence(sample)
        res.append(sample)

    return res, graph_voc


"""
ATC
"""


def build_atc_tree(unique_codes):
    res = []
    graph_voc = Voc()
    unique_codes = [code[2:] for code in unique_codes]

    root_node = 'atc_root'
    for code in unique_codes:
        sample = [code] + [code[:i] for i in [4, 3, 1]] + [root_node]

        graph_voc.add_sentence(sample)
        res.append(sample)

    return res, graph_voc
