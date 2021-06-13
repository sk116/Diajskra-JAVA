import java.util.ArrayList;                  // import all packages for given program
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;

//SHREE GANESHAY NAMAH:
class Adjecents                    // creating adgecents class structure for adjecents lists 
{
    int Node_No ;
    int Available_Seats ;
    int Weight ;
    Adjecents Adj_Ptr ;
}

class Node_Table_Directory         // creating node table directory to point each adjecents lists
{
    int Node_No ;
    String City_Name ;
    Adjecents List_Ptr ;
    Node_Table_Directory reverse ;
    int dst_frm_src ;
}

class Operation 
{
    Node_Table_Directory node[] = new Node_Table_Directory[17] ;
    ArrayList<Integer> path ;
    int sum_of_Distance = 0 ;
    public void Initialize()                        // in this method it intialize all nodes having object of Node_Table_Directory 
    {
        for( int i=0 ; i<17 ; i++ )
        {
            node[i] = new Node_Table_Directory() ;
            node[i].Node_No = i+1 ;
            node[i].List_Ptr = new Adjecents() ;
            node[i].dst_frm_src = Integer.MAX_VALUE ;
            node[i].List_Ptr.Adj_Ptr = null ;
            node[i].reverse = null ;
        }
        node[0].City_Name = "Bengaluru" ;
        node[1].City_Name = "Chennai" ;
        node[2].City_Name = "Hydrabad" ;
        node[3].City_Name = "Kochi" ;
        node[4].City_Name = "Madurai" ;
        node[5].City_Name = "Pondicherry" ;
        node[6].City_Name = "Mysore" ;
        node[7].City_Name = "Coimbatore" ;
        node[8].City_Name = "Ooty" ;
        node[9].City_Name = "Tirumala" ;
        node[10].City_Name = "Wazhi" ;
        node[11].City_Name = "Vizag" ;
        node[12].City_Name = "Hampi" ;
        node[13].City_Name = "Port-Blair" ;
        node[14].City_Name = "Vellore" ;
        node[15].City_Name = "Kovalam" ;
        node[16].City_Name = "Erode" ;
    }

    void Create_Adg( int NodeNum1 , int NodeNum2 , int dst )    // it will take two nodes and insert it into each others adjecents list sorted by weight of edge
    {
        Adjecents adj1 = new Adjecents() ;
        adj1.Node_No = NodeNum2 ;
        adj1.Available_Seats = 7 ;
        adj1.Weight = dst ; 
        adj1.Adj_Ptr = null ;
        Adjecents pre_temp = node[ NodeNum1 - 1 ].List_Ptr ;
        Adjecents temp = node[ NodeNum1 - 1 ].List_Ptr.Adj_Ptr ;
        if( temp == null )
        {
            pre_temp.Adj_Ptr = adj1 ;
        }
        else
        {
            while( temp != null && dst > temp.Weight )
            {
                pre_temp = temp ;
                temp = temp.Adj_Ptr ;
            }
            adj1.Adj_Ptr = pre_temp.Adj_Ptr ;
            pre_temp.Adj_Ptr = adj1 ;
        }
        Adjecents adj2 = new Adjecents() ;
        adj2.Node_No = NodeNum1 ;
        adj2.Available_Seats = 7 ;
        adj2.Weight = dst ;
        adj2.Adj_Ptr = null ;
        Adjecents pre_temp1 = node[ NodeNum2 - 1 ].List_Ptr ;
        Adjecents temp1 = node[ NodeNum2 - 1 ].List_Ptr.Adj_Ptr ;
        if ( temp1 == null )
            pre_temp1.Adj_Ptr = adj2 ; 
        else 
        {
            while ( temp1 != null && dst > temp1.Weight ) 
            {
                pre_temp1 = temp1 ;
                temp1 = temp1.Adj_Ptr ;
            }
            adj2.Adj_Ptr = pre_temp1.Adj_Ptr ;
            pre_temp1.Adj_Ptr = adj2 ;
        }
    }

    int Dijkstra( int source , int dest , int no_Of_seats )  // this method takes source and destination ..and performs dijkstra algorithm 
    {                                                        // it also sets reverse pointers towards the source whenever needs
        node[ source - 1 ].dst_frm_src = 0 ;
        node[ source - 1 ].reverse = node[ source - 1 ] ;
        LinkedList<Integer> S = new LinkedList<Integer>() ;
        LinkedList<Integer> V = new LinkedList<Integer>() ;
        LinkedList<Integer> Q = new LinkedList<Integer>() ;
        for ( int i=0 ; i<17 ; i++ )
        {    
            V.add(i+1) ;
            Q.add(i+1) ;
        }
        while( !Q.isEmpty() )
        {
            int u = 0 ;
            int min = Integer.MAX_VALUE ;
            for ( int i=0 ; i<Q.size() ; i++ )
            {
                if ( min>node[ Q.get(i) - 1 ].dst_frm_src )
                {
                    min = node[ Q.get(i) - 1 ].dst_frm_src ;
                    u = Q.get(i) ;
                }
            }
            Q.remove( Q.indexOf(u) ) ;
            S.add(u) ;
            Adjecents temp = node[ u-1 ].List_Ptr.Adj_Ptr ;
            while( temp != null )
            {
                if ( node[ temp.Node_No - 1 ].dst_frm_src > node[ u-1 ].dst_frm_src + temp.Weight )
                {
                    node[ temp.Node_No - 1 ].reverse = node[ u-1 ] ;
                    node[ temp.Node_No - 1 ].dst_frm_src = node[ u-1 ].dst_frm_src + temp.Weight ;
                }
                temp = temp.Adj_Ptr ;
            }
        }
        path = new ArrayList<Integer>() ;
        ShortestPath( source , dest ) ;
        sum_of_Distance = node[ dest - 1 ].dst_frm_src ;
        for ( int i=0 ; i<path.size()-1 ; i++ )
        {
            Adjecents temp = node[ path.get(i) - 1 ].List_Ptr.Adj_Ptr ;
            while( temp.Node_No != path.get(i+1) )
            {
                temp = temp.Adj_Ptr ;
            }
            temp.Available_Seats = temp.Available_Seats - no_Of_seats ;
            if ( temp.Available_Seats < 0 )
            {
                temp.Available_Seats += no_Of_seats ;
                return(-1) ;
            }
        }
        return(1) ;
    }

    void ShortestPath( int source , int dest )                // this method is for retrive path between souce and destination with minimum cost using reverse pointer
    {
        Node_Table_Directory tp = node[ dest - 1 ] ;
        if( tp != node[ source - 1 ] )
            ShortestPath( source , node[dest-1].reverse.Node_No ) ;
        path.add(tp.Node_No) ;
    }
}

class Contains_main
{
    int source ;
    int dest ;
    public static void main( String args[] ) throws IOException
    {
        Operation obj = new Operation() ;
        obj.Initialize() ;
        obj.Create_Adg( 1 , 2 , 13 ) ;             // creating adges by calling Create_adg function
        obj.Create_Adg( 1 , 3 , 89 ) ;
        obj.Create_Adg( 1 , 4 , 8 ) ;
        obj.Create_Adg( 1, 5 , 9 ) ;
        obj.Create_Adg( 1 , 6 , 83 ) ;
        obj.Create_Adg( 1 , 7 , 47 ) ;
        obj.Create_Adg( 1 , 11 , 100 ) ;
        obj.Create_Adg( 1 , 12 , 33 ) ;
        obj.Create_Adg( 2 , 3 , 17 ) ;
        obj.Create_Adg( 2 ,4 , 49 ) ;
        obj.Create_Adg( 2 , 5 , 37 ) ;
        obj.Create_Adg( 2 , 11 , 117 ) ;
        obj.Create_Adg( 2 , 14 , 131 ) ;
        obj.Create_Adg( 2 , 15 , 129 ) ;
        obj.Create_Adg( 3 , 6 , 12 ) ;
        obj.Create_Adg( 3 , 7 , 2 ) ;
        obj.Create_Adg( 3 , 11 , 11 ) ;
        obj.Create_Adg( 3 , 12 , 44 ) ; 
        obj.Create_Adg( 3 , 8 , 51 ) ;
        obj.Create_Adg( 3 , 9 , 49 ) ;
        obj.Create_Adg( 8 , 12 , 39 ) ; 
        obj.Create_Adg( 9 ,12 , 46 ) ;
        obj.Create_Adg( 10 , 11 , 30 ) ;
        obj.Create_Adg( 10 , 12 , 13 ) ;
        obj.Create_Adg( 11 , 12 , 33 ) ;
        obj.Create_Adg( 11 , 13 , 67 ) ;
        obj.Create_Adg( 11 , 14 , 12 ) ;
        obj.Create_Adg( 1 , 17 , 3 ) ;
        obj.Create_Adg( 2 , 17 , 45 ) ;
        obj.Create_Adg( 3 , 17 , 44 ) ;
        obj.Create_Adg( 11 , 15 , 13 ) ;
        obj.Create_Adg( 12 , 13 , 6 ) ;
        obj.Create_Adg( 11 , 16 , 111 ) ;
        obj.Create_Adg( 12 , 16 , 8 ) ; 
        obj.Create_Adg( 16 , 17 , 5 ) ;
        System.out.println("WELCOME : ") ;
        int cont = 0 ;
        int code = 0 ;
        for ( int i=0 ; i<17 ; i++ )
        {
            System.out.println( "(" + (i+1) + ")  " + obj.node[i].City_Name ) ;
        }
        do
        {
            System.out.print("choose the Source : ") ;
            Scanner sc = new Scanner( System.in ) ;
            int source = sc.nextInt() ;
            System.out.print("choose the Destination : ") ;
            int dest = sc.nextInt() ;
            System.out.print("Enter the number of seats you need : ") ;
            int no_of_seats = sc.nextInt() ;    
            code = obj.Dijkstra( source , dest , no_of_seats ) ;
            if( code == 1 )
            {
                System.out.print("Path of your journey is : ") ;
                for ( int i=0 ; i<obj.path.size() ; i++ )
                {
                    System.out.print( obj.node[ obj.path.get(i) - 1 ].City_Name + "-->" ) ;
                }
                System.out.println() ;
                System.out.println("Enter your contact details...") ;
                System.out.print("Name : ") ;
                String name = sc.next() ;
                System.out.print("Contact Number : ") ;
                String no = sc.next() ;
                FileWriter fl = new FileWriter( "Passenger_Info.txt" , true ) ; // insert passenger details in files 
                fl.write("\n" + name + ":" + no + "    Source : " + obj.node[source - 1].City_Name + "  " + "  Destination : " + obj.node[dest - 1].City_Name + "    Number of seats :- " + no_of_seats);
                fl.write(13) ;
                fl.close();
                System.out.println("Your travelling distance is " + obj.sum_of_Distance + " kms.") ;
                System.out.println("Your total cost of journey is " + obj.sum_of_Distance*17 + " Rupees.") ;
            }
            else if ( code == -1 )
            {
                System.out.println("These many seats are not available for given route.") ;
            }
            System.out.println("enter (1) to travel new journey...");
            cont = sc.nextInt() ;
        }
        while( cont == 1 ) ;
    }
}