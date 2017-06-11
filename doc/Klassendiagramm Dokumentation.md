# Klassendiagramm Dokumentation

## Converter
Die statische Klasse Converter, enthält die hauptsächliche Logik des Programmes, sei es die Umwandlung von IP-Addressen oder auch die Überprüfung der Netzwerke.  
Da der Converter nur statische Methoden enthält, können diese Methoden ohne Instanzierung der Klasse aufgerufen werden.  
Die Methoden *convertIpToBinary* und *convertIpToHex* dienen dazu dezimale IPAddressen in Hex oder auch Binär umzuwandeln.
Die Methode *checkIfPossibleNewNetwork* wird vom *NetworkPanel* genutzt um zu überprüfen ob die Netzwerke sich überlappen, dazu wird die Methode *isColliding* verwendet, welche zwei Netzwerke vergleicht. Um die Überprüfung zu erleichtern gibt es die Methode *IpToLong*, welche die IP-Addressen als Strings in long Werte umwandelt, so können die IP-Adressen von den Methoden besser verwaltet werden.
Die Methode *getAllIPsInNetwork* hat zwei verschiedene Anwendungsfälle einmal um die Die Host-IP-Adressen anzuzeigen und einmal um die Anzahl der freien IP-Addressen für das jeweilige Netzwerk zu ermitteln.
Die Subnetzmaske bilden wir aus den Präfix, dieser wiederum kann mittels zweier Methoden ermittelt werden, *getPrefixFromAmountOfHosts* und *getPrefixFromCompleteNetwork*.
Die Methode *getNewFreeIPAfterNetwork* wird dazu verwendet um im SubnetPanel zu ermitteln welche IP-Addresse für das nächste Subnetz frei wäre.
Damit die Subnetze auch richtig geordnet angezeigt werden, gibt es hierfür die Methode *sortNetworksInModel*, sodass die Netzwerke nach ihrer Größe sortiert werden.
