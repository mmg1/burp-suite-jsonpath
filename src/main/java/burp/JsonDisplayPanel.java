package burp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author augustd
 */
public class JsonDisplayPanel extends javax.swing.JPanel {

	JsonEntry entry;
	DefaultListModel<JsonFormatter.PathTuple> listModel = new DefaultListModel<>();
	JsonPathPanel jsonPathPanel;
	IBurpExtenderCallbacks callbacks;
	List<Integer> searchMatches = new ArrayList<>();
	int currentMatch = 0; 
	
	/**
	 * Creates new form JsonDisplayPanel
	 * @param entry
	 */
	public JsonDisplayPanel(JsonEntry entry, JsonPathPanel jsonPathPanel) {
		this.entry = entry;
		this.jsonPathPanel = jsonPathPanel;
		
		initComponents();
		
		//create the list elements
        for (JsonFormatter.PathTuple tuple : entry.formatter.getLines()) {
            listModel.addElement(tuple);
        }
		
		jsonList.addListSelectionListener(new JsonListSelectionListener(jsonPathPanel)); 
		
		callbacks = BurpExtender.getCallbacks(); 
		callbacks.customizeUiComponent(this);
		callbacks.customizeUiComponent(nextButton);
		callbacks.customizeUiComponent(prevButton);
		
	}

	class JsonListSelectionListener implements ListSelectionListener {

        //private final JList<JsonFormatter.PathTuple> jsonList;
        private final JsonPathPanel jsonPathPanel;
        
        public JsonListSelectionListener(JsonPathPanel jsonPathPanel) {
            //this.jsonList = jsonList;
            this.jsonPathPanel = jsonPathPanel;
        }
        
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JsonFormatter.PathTuple selectedValue = jsonList.getSelectedValue();
            System.out.println("Selected: " + selectedValue);
            jsonPathPanel.setJsonPathEntry(selectedValue.path);
        }
    }

	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jsonList = new javax.swing.JList<JsonFormatter.PathTuple>();
        prevButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        searchField = new javax.swing.JTextField();
        matchesLabel = new javax.swing.JLabel();

        jsonList.setModel(listModel);
        jsonList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jsonList);

        prevButton.setText("<");
        prevButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prevButtonMouseClicked(evt);
            }
        });

        nextButton.setText(">");
        nextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nextButtonMouseClicked(evt);
            }
        });

        searchField.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        searchField.setText("Type a search term");
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchFieldFocusGained(evt);
            }
        });
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchFieldKeyTyped(evt);
            }
        });

        matchesLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        matchesLabel.setText("0 matches");
        matchesLabel.setToolTipText("");
        matchesLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(prevButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(matchesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(prevButton)
                        .addComponent(nextButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(matchesLabel)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchFieldFocusGained
		if ("Type a search term".equals(searchField.getText())) {
			searchField.setText("");
		}
    }//GEN-LAST:event_searchFieldFocusGained

    private void searchFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyTyped
        String searchText = searchField.getText();
		Pattern searchPattern = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE);
		
		//look for matches of the search text in the list
		searchMatches.clear();
		currentMatch = 0;
		int index = 0;
		for (JsonFormatter.PathTuple tuple : java.util.Collections.list(listModel.elements())) {
			if (searchPattern.matcher(tuple.line).find()) { 
				searchMatches.add(index);
			}
			index++;
        }
		if (searchMatches.size() > 0) jsonList.setSelectedIndex(searchMatches.get(currentMatch));
		matchesLabel.setText(searchMatches.size() + " matches");
    }//GEN-LAST:event_searchFieldKeyTyped

    private void nextButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextButtonMouseClicked
        incrementCurrentMatch(1);
		jsonList.setSelectedIndex(searchMatches.get(currentMatch));
		jsonList.ensureIndexIsVisible(jsonList.getSelectedIndex());
    }//GEN-LAST:event_nextButtonMouseClicked

    private void prevButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prevButtonMouseClicked
        decrementCurrentMatch();
		jsonList.setSelectedIndex(searchMatches.get(currentMatch));
		jsonList.ensureIndexIsVisible(jsonList.getSelectedIndex());
    }//GEN-LAST:event_prevButtonMouseClicked

	private void incrementCurrentMatch(int amount) {
		currentMatch += amount; 
		currentMatch = currentMatch % searchMatches.size();  //TODO only works for positive increment
	}
	private void decrementCurrentMatch() {
		currentMatch-- ; //TODO allow for any negative increment
		if (currentMatch < 0) currentMatch = searchMatches.size()-1;
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<JsonFormatter.PathTuple> jsonList;
    private javax.swing.JLabel matchesLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton prevButton;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
}
