package com.example.tubes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes.database.Constant
import com.example.tubes.main_fragment.PlansFragment
import com.example.tubes.room.jadwal.Jadwal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

//class RVPlanAdapter(private val data: Array<Jadwal>) : RecyclerView.Adapter<RVPlanAdapter.viewHolder>() {
class RVPlanAdapter(
    private var data: List<Jadwal>, context: Context, fragment: Fragment
) : RecyclerView.Adapter<RVPlanAdapter.viewHolder>(), Filterable {
    private val context: Context
    private val fragment: Fragment
    private var filteredList: MutableList<Jadwal>

    init {
        filteredList = ArrayList<Jadwal>(data)
        this.context = context
        this.fragment = fragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_plan, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = filteredList[position]
        Log.d("logggggg", currentItem.toString())
        holder.tvNama.text = currentItem.namaTrainer
        holder.tvWorkout.text = currentItem.plan
        holder.tvMulai.text = currentItem.jamMulai
        holder.tvAkhir.text = currentItem.jamAkhir

        holder.btnDelete.setOnClickListener{
            val dialog = MaterialAlertDialogBuilder(context)
            dialog.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin untuk delete?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_,_ ->
                    if(fragment is PlansFragment) currentItem.id?.let{ it1 ->
                        fragment.deletePlan(
                            it1
                        )
                    }
                }
                .show()
        }

        holder.btnEdit.setOnClickListener{
            context?.startActivity(
                Intent(context, EditPlansActivity::class.java)
                    .putExtra("intent_id", currentItem.id)
                    .putExtra("intent_type", Constant.TYPE_UPDATE)
            )
            notifyItemChanged(position)
            notifyDataSetChanged()
        }

//        holder.btnCetak.setOnClickListener{
//            val dialog = MaterialAlertDialogBuilder(context)
//            dialog.setTitle("Konfirmasi")
//                .setMessage("Apakah anda yakin untuk membuat PDF?")
//                .setNegativeButton("Batal", null)
//                .setPositiveButton("Buat"){_,_ ->
//                    createPdf(namaTrainer, jamMulai, jamAkhir, currentItem.id)
//               }
//                .show()
//        }
    }

    fun setData(data2: Array<Jadwal>) {
        this.data = data2.toList()
        filteredList = data2.toMutableList()
    }
    override fun getItemCount(): Int {
        return filteredList.size
    }

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvNama : TextView = itemView.findViewById(R.id.namaTrainer)
        val tvWorkout : TextView = itemView.findViewById(R.id.workoutPlan)
        val tvMulai : TextView = itemView.findViewById(R.id.jamMulai)
        val tvAkhir : TextView = itemView.findViewById(R.id.jamAkhir)
        val btnDelete : Button = itemView.findViewById(R.id.btnDelete)
        val btnEdit : Button = itemView.findViewById(R.id.btnEdit)
        val btnCetak : Button = itemView.findViewById(R.id.btnCetak)
    }
    override fun getFilter(): Filter {
        return object : Filter (){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Jadwal> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(data)
                }
                else{
                    for(jadwal in data){
                        if(jadwal.namaTrainer.lowercase(Locale.getDefault()).contains(charSequenceString.lowercase(Locale.getDefault()))){
                            filtered.add(jadwal)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults : FilterResults){
                filteredList.clear()
                filteredList.addAll(filterResults.values as List<Jadwal>)
                notifyDataSetChanged()
                Log.d("bindviewpub", filteredList.size.toString())
            }
        }

    }

//    @SuppressLint("ObsoleteSdkInt")
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Throws(FileNotFoundException::class)
//    private fun createPdf(namaTrainer: String, plan: String, jamMulai: String, jamAkhir: String, index: Int){
//        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
//        val file = File(pdfPath, "Jadwal_Latihan_" + index + ".pdf")
////        val file = File(pdfPath, "Jadwal_Latihan.pdf")
//        FileOutputStream(file)
//
//        val writer = PdfWriter(file)
//        val pdfDoc = PdfDocument(writer)
//        val doc = Document(pdfDoc)
//        pdfDoc.defaultPageSize = PageSize.A4
//        doc.setMargins(5f, 5f, 5f, 5f)
////        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.removedbg)
//        val bitmap = (d as BitmapDrawable?)!!.bitmap
//        val stream = ByteArrayOutputStream()
//        sizebitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        val bitmapData = stream.toByteArray()
//        val imageData = ImageDataFactory.create(bitmapData)
//        val image = Image(imageData)
//        val group = Paragraph(
//            """
//                Data Workout Plans:
//            """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)
//
//        val width = floatArrayOf(100f, 100f)
//        val table = Table(width)
//
//        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
//        table.addCell(Cell().add(Paragraph("Nama Trainer")))
//        table.addCell(Cell().add(Paragraph(namaTrainer)))
//        table.addCell(Cell().add(Paragraph("Workout Plan")))
//        table.addCell(Cell().add(Paragraph(plan)))
//        table.addCell(Cell().add(Paragraph("Jam Mulai")))
//        table.addCell(Cell().add(Paragraph(jamMulai)))
//        table.addCell(Cell().add(Paragraph("Jam Akhir")))
//        table.addCell(Cell().add(Paragraph(jamAkhir)))
//        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        table.addCell(Cell().add(Paragraph("Tanggal Pembuatan Plan")))
//        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
//
//        val barcodeQRCode = BarcodeQRCode(
//            """
//                                        $namaTrainer
//                                        $plan
//                                        $jamMulai
//                                        $jamAkhir
//                                        ${LocalDate.now().format(dateTimeFormatter)}
//                                        ${Local1Time.now().format(timeFormatter)}
//                                        """.trimIndent())
//        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
//        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)
//
//
//        doc.add(group)
//        doc.add(table)
//        doc.add(qrCodeImage)
//
//        doc.close()
//        Toast.makeText(this, "PDF Created", Toast.LENGTH_SHORT).show()
//    }

}