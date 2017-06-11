# Klassendiagramm Dokumentation

## Converter
Die statische Klasse Converter, enthält die hauptsächliche Logik des Programmes, sei es die Umwandlung von IP-Addressen oder auch die Überprüfung der Netzwerke.  
Da der Converter nur statische Methoden enthält, können diese Methoden ohne Instanzierung der Klasse aufgerufen werden.  
Die Methoden convertIpToBinary und convertIpToHex dienen dazu dezimale IPAddressen in Hex oder auch Binär umzuwandeln.
Die Methode *checkIfPossibleNewNetwork* wird vom *NetworkPanel* genutzt um zu überprüfen ob die Netzwerke sich überlappen, dazu wird die Methode *isColliding* verwendet, welche zwei Netzwerke vergleicht. Um die Überprüfung zu erleichtern gibt es die Methode *IpToLong*, welche die IPAddressen als Strings in long Werte umwandelt, so können die IPAdressen von den Methoden besser verwaltet werden.
Die Methode *getAllIPsInNetwork* hat zwei verschiedene Anwendungsfällt einmal um die Die Host-IPadressen anzuzeigen und einmal um die Anzahl der freien IpAddressen für das jeweilige Netzwerk zu ermitteln.
