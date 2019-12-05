package com.example.digitalreceipts.Finance;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.digitalreceipts.Database.ReceiptRepository;
import com.example.digitalreceipts.Database.ReceiptsManager;
import com.example.digitalreceipts.MainActivity.ReceiptsRoom;
import com.example.digitalreceipts.R;
import com.google.android.material.textfield.TextInputEditText;

public class CategoriseDialog extends DialogFragment {
    ReceiptsManager receiptsManager;
    PopupWindow popupWindow;
    private CategoriseDialog(){}

    public static CategoriseDialog newInstance(ReceiptsRoom receipts, PopupWindow popupWindow) {

        Bundle args = new Bundle();
        args.putParcelable("RECEIPTS",receipts);
        CategoriseDialog fragment = new CategoriseDialog();
        fragment.popupWindow = popupWindow;
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categorise_dialog, container, false);
        TextView text = rootView.findViewById(R.id.categorise_text);
        Button button = rootView.findViewById(R.id.update_category);
        TextInputEditText textInputEditText = rootView.findViewById(R.id.text_input);
        receiptsManager = ViewModelProviders.of(this).get(ReceiptsManager.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expense_type = textInputEditText.getText().toString();
                ReceiptsRoom receipts = getArguments().getParcelable("RECEIPTS");
                receipts.set_expenseType(expense_type);
                receiptsManager.update(receipts);
                textInputEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new FinanceFragment()).commit();
                popupWindow.dismiss();
                dismiss();

            }
        });

        return rootView;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity fragment = getActivity();
        if(fragment instanceof DialogInterface.OnDismissListener){
            ((DialogInterface.OnDismissListener) fragment).onDismiss(dialog);
        }
    }
}
