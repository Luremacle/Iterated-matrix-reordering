library(seriation)
args <- commandArgs(TRUE)
meth <- as.character(args[1])

ser <- seriate(x = as.dist(read.table(file="R/temp.txt")), method = meth)
order1 <- get_order(ser[1])
write.csv(order1, paste("R/","row.order",sep=""), row.names=FALSE)
dev.off()
