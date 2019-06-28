package blockchain;

import java.util.ArrayList;
import java.util.Date;
import com.google.gson.GsonBuilder;

public class Block {
	
	public String hash;
	public String previousHash;
	private String data;
	private long timeStamp;
	private int nonce;
	
	//Variaveis da blockchains
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static int difficulty = 5;

	public Block(String data, String previousHash) {
		
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
		
	}
	
	public String calculateHash() {
		
		String calculatedhash = StringUtil.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);
		
		return calculatedhash;
	}

	public static boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		//loop através da blockchain para verificar os hashes
		for(int i = 1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			
			//Compara o hash registrado no block com o hask calculado
			if(!currentBlock.hash.equals(currentBlock.calculateHash())) {
				System.out.println("Hash atual não é igual");
				return false;
			}
			
			//Compara o hash do bloco anterior com o hash calculado para o bloco anterior
			if(!previousBlock.hash.equals(previousBlock.calculateHash())) {
				System.out.println("Hash anterior não é igual");
				return false;
			}
			
			//Verifica se o hash foi resolvido
			if(!currentBlock.hash.substring(0,difficulty).equals(hashTarget)) {
				System.out.println("Esse Bloco não foi minerado!!");
				return false;
			}
		}
		
		return true;	
	}
	
	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0'); //Cria uma string com dificulda * '0' (example: "000")
		while(!hash.substring(0,difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		
		System.out.println("Block MINERADO!!!: " + hash);
	}
	
	public static void main(String[] args) {
		
		//Adicionando novos blocos a blockchain
		blockchain.add(new Block("VOTO: JOAO", "0"));
		System.out.println("Tentando minerar o bloco 1 ...");
		blockchain.get(0).mineBlock(difficulty);
		
		blockchain.add(new Block("VOTO: JOSE", blockchain.get(blockchain.size()-1).hash));
		System.out.println("Tentando minerar o bloco 2 ...");
		blockchain.get(1).mineBlock(difficulty);
		
		blockchain.add(new Block("VOTO: MARIA", blockchain.get(blockchain.size()-1).hash));
		System.out.println("Tentando minerar o bloco 3 ...");
		blockchain.get(2).mineBlock(difficulty);
		
		System.out.println("\nBlockchain é valida? : " + isChainValid());
		
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println("\n------BLOCKCHAIN-------");
		System.out.println(blockchainJson);
		
	}
	
}
