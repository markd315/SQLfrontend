package JDBCSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Main extends javax.swing.JFrame {
    
	private static final long serialVersionUID = 1L;
	/** Creates new form */
    public Main() {
    	debugLabel = new javax.swing.JLabel();
        nameSelector = new javax.swing.JComboBox<String>();
        queryButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        insertButton = new javax.swing.JButton();
        queryResponse = new javax.swing.JTextArea();
        flowerLabel = new javax.swing.JLabel();
        sightingLabel = new javax.swing.JLabel();
        flowerGenus = new javax.swing.JTextArea();
        flowerSpecies = new javax.swing.JTextArea();
        flowerComname = new javax.swing.JTextArea();
        sightingName = new javax.swing.JTextArea();
        sightingPerson = new javax.swing.JTextArea();
        sightingLocation = new javax.swing.JTextArea();
        sightingDate = new javax.swing.JTextArea();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SQLite frontend");

        queryButton.setText("Query");
        queryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queryButtonActionPerformed(evt);
            }
        });
        
        insertButton.setText("Insert");
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });
        
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        debugLabel.setText("Run a query!");
        sightingLabel.setText("Insert sighting: name, person, location, YYYY-MM-DD");
        flowerLabel.setText("Update flower: genus, species, comname. Make sure to select in the dropdown!");
        
        
        updateNameSelector();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(debugLabel))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(flowerLabel))
                    .addGroup(layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(flowerGenus)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(flowerSpecies)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(flowerComname)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(updateButton))
                    .addGroup(layout.createSequentialGroup()
                    		.addComponent(sightingLabel))
                    .addGroup(layout.createSequentialGroup()
                    		.addComponent(sightingName)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(sightingPerson)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(sightingLocation)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(sightingDate)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(insertButton))
                	.addGroup(layout.createSequentialGroup()
                        .addComponent(queryButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(queryResponse)
                .addContainerGap(27, Short.MAX_VALUE)))
        ));
        
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {queryButton, nameSelector});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                	.addComponent(debugLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(flowerLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(flowerGenus)
                        .addComponent(flowerSpecies)
                        .addComponent(flowerComname)
                        .addComponent(updateButton))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sightingLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sightingName)
                        .addComponent(sightingPerson)
                        .addComponent(sightingLocation)
                        .addComponent(sightingDate)
                        .addComponent(insertButton))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(queryResponse))        
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(queryButton))
                
        ));
        pack();
    }
    
    private void updateNameSelector() {
    	String[] rets = pullNames();
        for(String single : rets) {
        	nameSelector.addItem(single);
        }
	}

	private void queryButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	queryContents = getQuery((String) nameSelector.getSelectedItem());
    	String lineDelimited = "";
    	for(String s : queryContents) {
    		lineDelimited+=s;
    		lineDelimited+="\n";
    	}
    	queryResponse.setText(lineDelimited);
    	debugLabel.setText("Repopulated list: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
    }
    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	String sql = "UPDATE flowers SET genus = ? , "
                + "species = ? ,"
    			+ "comname = ?"
                + "WHERE comname = ?";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setString(1, flowerGenus.getText());
            pstmt.setString(2, flowerSpecies.getText());
            pstmt.setString(3, flowerComname.getText());
            pstmt.setString(4, (String) nameSelector.getSelectedItem());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	updateNameSelector();
    	debugLabel.setText("Updated item: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
    }
    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	String sql = "INSERT INTO sightings(name,person,location,sighted) VALUES(?,?,?,?)";
    	 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sightingName.getText());
            pstmt.setString(2, sightingPerson.getText());
            pstmt.setString(3, sightingLocation.getText());
            pstmt.setString(4, sightingDate.getText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	debugLabel.setText("Inserted item: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
    }
    private String[] getQuery(String queryName) {
    	String sql = "SELECT * FROM sightings WHERE \"" + queryName + "\"=name ORDER BY sighted desc LIMIT 10";
        ArrayList<String> resList = new ArrayList<String>();
    	
        try (Connection conn = this.connect();
            	Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                resList.add(rs.getString("name") +  "\t" + 
                                   rs.getString("person") + "\t" +
                                   rs.getString("location") + "\t" /*+
                                   rs.getDate("sighted")*/); //Commented date out because I was getting "error parsing time stamp"
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String[] arr = (String[]) resList.toArray(new String[resList.size()]);
    	return arr;
    }
    private String[] pullNames() {
    	String sql = "SELECT comname FROM flowers;";
        ArrayList<String> resList = new ArrayList<String>();
    	
        try (Connection conn = this.connect();
        	Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                resList.add(rs.getString("comname"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String[] arr = resList.toArray(new String[resList.size()]);
    	return arr;
	}
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:db/a4.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void main(String args[]) {
    	java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    private static Connection conn;
    private javax.swing.JComboBox<String> nameSelector;
    private javax.swing.JButton queryButton;
    private javax.swing.JTextArea queryResponse;
    private javax.swing.JButton updateButton;
    private javax.swing.JButton insertButton;
    private javax.swing.JLabel debugLabel;
    
    //To connect queryResponse to flowerIndex:
    private String[] queryContents;
  
    //For updates
    
    private javax.swing.JLabel flowerLabel;
    private javax.swing.JTextArea flowerGenus;
    private javax.swing.JTextArea flowerSpecies;
    private javax.swing.JTextArea flowerComname;
    
    //For inserts
    private javax.swing.JLabel sightingLabel;
    private javax.swing.JTextArea sightingName;
    private javax.swing.JTextArea sightingPerson;
    private javax.swing.JTextArea sightingLocation;
    private javax.swing.JTextArea sightingDate;
}